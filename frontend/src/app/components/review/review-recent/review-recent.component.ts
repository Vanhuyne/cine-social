import { Component, Input } from '@angular/core';
import { ReviewRecentResponse } from '../../../models/Review';
import { Router } from '@angular/router';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-review-recent',
  templateUrl: './review-recent.component.html',
  styleUrl: './review-recent.component.scss'
})
export class ReviewRecentComponent {
  @Input() reviews: ReviewRecentResponse[] = [];
  @Input() isLoading = false;

  constructor(private router: Router) {}

  getProfileImageUrl(fileName: string | undefined): string {
      if (!fileName) return '';
      return `${environment.apiUrl}/users/image/${fileName}`;
    }
  // formatTime(date: Date): string {
  //   return formatDistanceToNow(new Date(date), { addSuffix: true });
  // }
}
