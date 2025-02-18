import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ReviewResponse, ReviewRequest, PageResponse } from '../../models/Review';
import { ReviewService } from '../../service/review.service';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { Subject, Subscription } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { VoteService } from '../../service/vote.service';
import { environment } from '../../environments/environment';
import { UserProfile } from '../../models/User';
import { UserService } from '../../service/user.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html'
})
export class ReviewComponent implements OnInit, OnDestroy {

  @Input() movieId!: number;
  reviews: ReviewResponse[] = [];

  // Pagination state
  currentPage: number = 0; // note: service uses 0-based pages
  totalPages: number = 0;
  pageSize: number = 5;
  isLoading: boolean = false;
  isModalOpen = false;

  private destroy$ = new Subject<void>();
  isLoggedIn = false;
  currentUser: UserProfile | null = null;

  constructor(
    private reviewService: ReviewService,
    private authService: AuthService,
    private router: Router,
    private voteService: VoteService,
    private userService: UserService
  ) {

  }

  ngOnInit(): void {
    this.setupAuthSubscriptions();
    this.loadReviews();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private setupAuthSubscriptions(): void {
    this.authService.isLoggedIn()
      .pipe(takeUntil(this.destroy$))
      .subscribe(loggedIn => {
        this.isLoggedIn = loggedIn;
      });

    this.userService.getCurrentUserProfile()
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => this.currentUser = user);
  }


  openReviewModal(): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/login'], {
        queryParams: { returnUrl: this.router.url }
      });
      return;
    }
    this.isModalOpen = true;
  }

  closeReviewModal(): void {
    this.isModalOpen = false;
  }

  private loadReviews(): void {
    this.isLoading = true;
    this.reviewService.getMovieReviews(this.movieId, this.currentPage, this.pageSize)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (pageResponse: PageResponse<ReviewResponse>) => {
          this.reviews = this.reviews.concat(pageResponse.content);
          this.totalPages = pageResponse.page.totalPages;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading reviews:', err);
          this.isLoading = false;
        }
      });
  }
  loadMore(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadReviews();
    }
  }

  handleReviewSubmit(reviewRequest: ReviewRequest): void {
    const completeRequest = {
      ...reviewRequest,
      movieId: this.movieId
    };

    this.reviewService.createReview(completeRequest)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (review) => {
          const reviewData = {
              ...review,
            userAvatar: this.currentUser?.profilePicture || '',  // Gán ảnh nếu có
            userDisplayName: this.currentUser?.username || 'Anonymous'
          }
          this.reviews.unshift(reviewData);
          console.log('Review:', reviewRequest.rating);
          this.closeReviewModal();
        },
        error: (err) => console.error('Error submitting review:', err)
      });

    
  }

  handleVote(reviewId: number, voteType: 'UP' | 'DOWN') {
    if (!this.isLoggedIn) {
      // Handle not logged in state - maybe show login prompt
      this.router.navigate(['/login']);
      return;
    }

    const voteRequest = { reviewId, voteType };

    this.voteService.vote(voteRequest).subscribe({
      next: (response) => {
        // Find the review in the array.
        const reviewIndex = this.reviews.findIndex(r => r.reviewId === reviewId);
        if (reviewIndex !== -1) {
          const review = this.reviews[reviewIndex];

          // If the user clicks the same vote button, remove their vote.
          if (review.userVote === voteType) {
            if (voteType === 'UP') {
              review.upVotes = Math.max(0, review.upVotes - 1);
            } else {
              review.downVotes = Math.max(0, review.downVotes - 1);
            }
            review.userVote = null;
          }
          // If the user is changing their vote (from UP to DOWN or vice versa).
          // If the user has already voted and is now switching the vote.
          else if (review.userVote) {
            if (voteType === 'UP') {
              review.upVotes++;
              review.downVotes = Math.max(0, review.downVotes - 1);
            } else {
              review.downVotes++;
              review.upVotes = Math.max(0, review.upVotes - 1);
            }
            review.userVote = voteType;
          }
          // If the user is voting for the first time.
          else {
            if (voteType === 'UP') {
              review.upVotes++;
            } else {
              review.downVotes++;
            }
            review.userVote = voteType;
          }

          // Reassign the review to trigger change detection.
          this.reviews[reviewIndex] = { ...review };
        }
      },
      error: (error) => {
        console.error('Error voting:', error);
        // Optionally, show an error message to the user.
      }
    });

  }

  getProfileImageUrl(fileName: string | undefined): string {
    if (!fileName) return '';
    return `${environment.apiUrl}/users/image/${fileName}`;
  }
}