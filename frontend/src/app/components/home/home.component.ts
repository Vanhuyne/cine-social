import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ReviewService } from '../../service/review.service';
import { ReviewRecentResponse } from '../../models/Review';
import { catchError, finalize, of } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  showMovieList: boolean = true;
  showMovieGird: boolean = true;
  recentReviews: ReviewRecentResponse[] = [];
  isLoading = false;
  error: string | null = null;

  constructor(
    private router: ActivatedRoute,
    private reviewService: ReviewService
  ) { }

  // check current route
  ngOnInit() {
    if (this.router.snapshot.url.map(segment => segment.path).includes('explore')) {
      this.showMovieList = false;
      this.showMovieGird = true
    } else {
      this.showMovieList = true;
      this.showMovieGird = false;
    }
    this.loadRecentReviews();
  }

  loadRecentReviews() {
    this.isLoading = true;
    this.error = null;
    this.reviewService.getRecentReviews().pipe(
      catchError(err => {
        this.error = 'Failed to load reviews';
        return of([]);
      }),
      finalize(() => this.isLoading = false)
    ).subscribe(reviews => {
      this.recentReviews = reviews;
    });
  }


}
