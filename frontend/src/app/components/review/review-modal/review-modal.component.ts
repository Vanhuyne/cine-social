import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ReviewRequest } from '../../../models/Review';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-review-modal',
  templateUrl: './review-modal.component.html',
  styleUrl: './review-modal.component.scss'
})
export class ReviewModalComponent {
  @Input() isOpen = false;
  @Input() movieId!: number; // Add movieId as input
  @Output() closeModal = new EventEmitter<void>();
  @Output() submitReview = new EventEmitter<ReviewRequest>();

  reviewForm: FormGroup;
  selectedRating = 0;

  constructor(private fb: FormBuilder) {
    this.reviewForm = this.fb.group({
      comment: ['', [Validators.required, Validators.maxLength(2000)]]
    });
  }

  setRating(rating: number): void {
    this.selectedRating = rating;
  }

  close(): void {
    this.reset();
    this.closeModal.emit();
  }

  onSubmit(): void {
    if (this.reviewForm.valid && this.selectedRating > 0) {
      const review: ReviewRequest = {
        movieId: this.movieId, // Include movieId in the review request
        rating: this.selectedRating,
        comment: this.reviewForm.value.comment
      };
      this.submitReview.emit(review);
      this.reset();
    }
  }

  private reset(): void {
    this.reviewForm.reset();
    this.selectedRating = 0;
  }

  
}
