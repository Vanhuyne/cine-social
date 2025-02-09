import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ReviewResponse, ReviewRequest, PageResponse } from '../../models/Review';
import { ReviewService } from '../../service/review.service';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { VoteService } from '../../service/vote.service';

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
  currentUser: string | null = null;

  constructor(
    private reviewService: ReviewService,
    private authService: AuthService,
    private router: Router,
    private voteService: VoteService
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

    this.authService.getCurrentUser()
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
          console.log('Reviews:', this.reviews);
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
          this.reviews.unshift(review);
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

    const voteRequest = {
      reviewId,
      voteType
    }

    // Call your vote service here
    this.voteService.vote(voteRequest).subscribe({
      next: (response) => {
        // Update the review in the reviews array
        const reviewIndex = this.reviews.findIndex(r => r.reviewId === reviewId);
        if (reviewIndex !== -1) {
          const review = this.reviews[reviewIndex];
          
          // If user is removing their vote
          if (review.userVote === voteType) {
            if (voteType === 'UP') review.upVotes--;
            if (voteType === 'DOWN') review.downVotes--;
            review.userVote = null;
          } 
          // If user is changing their vote
          else if (review.userVote) {
            if (voteType === 'UP') {
              review.upVotes++;
              review.downVotes--;
            } else {
              review.upVotes++;
              review.downVotes--;
            }
            review.userVote = voteType;
          }
          // If user is voting for the first time
          else {
            if (voteType === 'UP') review.upVotes++;
            if (voteType === 'DOWN') review.downVotes++;
            review.userVote = voteType;
          }
          
          this.reviews[reviewIndex] = { ...review };
        }
      },
      error: (error) => {
        console.error('Error voting:', error);
        // Handle error - maybe show error message
      }
    });
  }
}