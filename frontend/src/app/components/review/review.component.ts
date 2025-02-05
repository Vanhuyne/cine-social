import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { ReviewResponse, ReviewRequest, PageResponse } from '../../models/Review';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReviewService } from '../../service/review.service';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html'
})
export class ReviewComponent implements OnInit, OnDestroy {
  @Input() movieId!: number;
  reviews: ReviewResponse[] = [];
  
  // Pagination state
  currentPage = 0; // Spring Boot uses 0-based pagination
  pageSize = 5;
  totalPages = 0;
  totalElements = 0;
  isLoading = false;
  
  private destroy$ = new Subject<void>();
  isFormVisible = false;
  selectedRating = 0;
  reviewForm: FormGroup;
  isLoggedIn = false;
  currentUser: string | null = null;

  constructor(
    private reviewService: ReviewService,
    private authService: AuthService,
    private router: Router,
    private fb: FormBuilder
  ) {
    this.reviewForm = this.fb.group({
      comment: ['', [Validators.required, Validators.maxLength(2000)]]
    });
  }

  ngOnInit(): void {
    this.setupAuthSubscriptions();
    this.loadInitialReviews();
    this.setupInfiniteScroll();
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
        if (!loggedIn && this.isFormVisible) {
          this.isFormVisible = false;
        }
      });

    this.authService.getCurrentUser()
      .pipe(takeUntil(this.destroy$))
      .subscribe(user => this.currentUser = user);
  }

  private loadInitialReviews(): void {
    this.isLoading = true;
    this.reviewService.getMovieReviews(this.movieId)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.reviews = response.content;
          this.updatePaginationState(response);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading reviews:', err);
          this.isLoading = false;
        }
      });
  }

  private updatePaginationState(response: PageResponse<ReviewResponse>): void {
    this.currentPage = response.page.number;
    this.totalPages = response.page.totalPages;
    this.totalElements = response.page.totalElements;
    this.pageSize = response.page.size;
  }

  loadMoreReviews(): void {
    if (this.isLoading || this.currentPage >= this.totalPages - 1) return;

    this.isLoading = true;
    const nextPage = this.currentPage + 1;

    this.reviewService.getMovieReviews(this.movieId, nextPage, this.pageSize)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (response) => {
          this.reviews = [...this.reviews, ...response.content];
          this.updatePaginationState(response);
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Error loading more reviews:', err);
          this.isLoading = false;
        }
      });
  }

  private setupInfiniteScroll(): void {
    const options = {
      root: null,
      rootMargin: '0px',
      threshold: 0.5
    };

    const observer = new IntersectionObserver((entries) => {
      entries.forEach(entry => {
        if (entry.isIntersecting && !this.isLoading && this.currentPage < this.totalPages - 1) {
          this.loadMoreReviews();
        }
      });
    }, options);

    // Observe the sentinel element
    setTimeout(() => {
      const sentinel = document.querySelector('.sentinel');
      if (sentinel) {
        observer.observe(sentinel);
      }
    }, 0);
  }

  onSubmit(): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }

    if (this.reviewForm.valid && this.selectedRating > 0) {
      const reviewRequest: ReviewRequest = {
        movieId: this.movieId,
        rating: this.selectedRating,
        comment: this.reviewForm.value.comment
      };

      this.reviewService.createReview(reviewRequest)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (review) => {
            this.reviews.unshift(review);
            this.reviewForm.reset();
            this.selectedRating = 0;
            this.isFormVisible = false;
          },
          error: (err) => console.error('Error submitting review:', err)
        });
    }
  }
  
  setRating(rating: number): void {
    this.selectedRating = rating;
  }

  toggleReviewForm(): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/login'], {
        queryParams: { returnUrl: this.router.url }
      });
      return;
    }

    this.isFormVisible = !this.isFormVisible;
    if (!this.isFormVisible) {
      this.reviewForm.reset();
      this.selectedRating = 0;
    }
  }
  // ... rest of the component methods remain the same
}