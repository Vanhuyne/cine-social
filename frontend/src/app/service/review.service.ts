import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Review, ReviewRequest } from '../models/Review';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private baseUrl = 'http://localhost:8081/api/reviews';
  constructor(
    private http: HttpClient
  ) { }

  getMovieReviews(movieId: number): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.baseUrl}/movie/${movieId}`);
  }

  createReview(review: ReviewRequest): Observable<Review> {
    return this.http.post<Review>(`${this.baseUrl}`, review);
  }

  updateReview(reviewId: number, reviewRequest: ReviewRequest): Observable<Review> {
    return this.http.put<Review>(`${this.baseUrl}/${reviewId}`, reviewRequest);
  }

  deleteReview(reviewId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${reviewId}`);
  }

}
