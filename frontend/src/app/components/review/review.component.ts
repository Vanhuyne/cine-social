import { Component, Input } from '@angular/core';
import { Review, ReviewRequest } from '../../models/Review';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReviewService } from '../../service/review.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrl: './review.component.scss'
})
export class ReviewComponent {
  @Input() movieId!: number;
  reviews: Review[] = [];

  selectedRating = 0;
  reviewForm: FormGroup;

  constructor(
    private reviewService: ReviewService,
    private fb: FormBuilder
  ) {
    this.reviewForm = this.fb.group({
      comment: ['', [Validators.required, Validators.maxLength(2000)]]
    });
  }

  ngOnInit(): void {
    this.loadReviews();
  }

  setRating(rating: number): void {
    this.selectedRating = rating;
  }

  onSubmit(): void {
    if (this.reviewForm.valid && this.selectedRating > 0) {
      const reviewRequest: ReviewRequest = {
        movieId: this.movieId,
        rating: this.selectedRating,
        comment: this.reviewForm.value.comment
      };

      this.reviewService.createReview(reviewRequest).subscribe({
        next: (review) => {
          this.reviews.unshift(review);
          this.reviewForm.reset();
          this.selectedRating = 0;
        },
        error: (err) => console.error('Error submitting review:', err)
      });
    }
  }

  private loadReviews(): void {
    this.reviewService.getMovieReviews(this.movieId).subscribe({
      next: (reviews) => this.reviews = reviews,
      error: (err) => console.error('Error loading reviews:', err)
    });
  }


}
