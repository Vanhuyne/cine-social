import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WatchList } from '../models/WatchList';
import { WatchListDetail } from '../models/WatchListDetail';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {
  
  private readonly apiUrl = 'http://localhost:8081/api/watchlist';
  constructor(
    private http: HttpClient
  ) { }

  getAllWatchlist() : Observable<WatchList[]> {
    return this.http.get<WatchList[]>(`${this.apiUrl}/user`);
  }


  getWatchListDedail(watchlistId: number) : Observable<WatchListDetail> {
    return this.http.get<WatchListDetail>(`${this.apiUrl}/${watchlistId}`);
  }

}
