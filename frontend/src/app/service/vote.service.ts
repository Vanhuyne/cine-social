import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { VoteRequest } from '../models/VoteRequest';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VoteService {
  private baseUrl = 'http://localhost:8081/api/vote'
  constructor(
    private http: HttpClient,
  ) { }

  vote(VoteRequest : VoteRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/upvote`, VoteRequest);
  }
}
