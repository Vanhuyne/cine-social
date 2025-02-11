import { Component, Input } from '@angular/core';
import { ReviewRecentResponse } from '../../../models/Review';
import { Router } from '@angular/router';

@Component({
  selector: 'app-review-recent',
  templateUrl: './review-recent.component.html',
  styleUrl: './review-recent.component.scss'
})
export class ReviewRecentComponent {
  @Input() reviews: ReviewRecentResponse[] = [];
  @Input() isLoading = false;

  constructor(private router: Router) {}

  // formatTime(date: Date): string {
  //   return formatDistanceToNow(new Date(date), { addSuffix: true });
  // }
}
