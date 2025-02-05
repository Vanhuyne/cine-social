import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageResponse, ReviewRequest, ReviewResponse } from '../models/Review';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private baseUrl = 'http://localhost:8081/api/reviews';
  constructor(
    private http: HttpClient
  ) { }

  getMovieReviews(movieId: number, page: number = 0, size: number = 5): Observable<PageResponse<ReviewResponse>> {
    // Note: Spring Boot uses 0-based page numbers
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    
    return this.http.get<PageResponse<ReviewResponse>>(`${this.baseUrl}/movie/${movieId}`, { params });
  }

  createReview(review: ReviewRequest): Observable<ReviewResponse> {
    return this.http.post<ReviewResponse>(`${this.baseUrl}`, review);
  }

  updateReview(reviewId: number, reviewRequest: ReviewRequest): Observable<ReviewResponse> {
    return this.http.put<ReviewResponse>(`${this.baseUrl}/${reviewId}`, reviewRequest);
  }

  deleteReview(reviewId: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${reviewId}`);
  }

}
