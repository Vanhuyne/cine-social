import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';

import {  WatchListDetail } from '../models/WatchListDetail';
import { Movie } from '../models/Movie';

@Injectable({
  providedIn: 'root'
})
export class WatchlistService {
  private readonly WATCHLIST_KEY = 'watchlist';
    private readonly DEFAULT_WATCHLIST_NAME = 'Your Watchlist';

    private watchlist: WatchListDetail = {
        watchlistId: 1,
        name: this.DEFAULT_WATCHLIST_NAME,
        movies: [],
    };

    private watchlistSubject = new BehaviorSubject<WatchListDetail>(this.watchlist);

    constructor() {
        this.loadFromLocalStorage();
    }

    // Load watchlist from local storage
    private loadFromLocalStorage(): void {
        const savedWatchlist = localStorage.getItem(this.WATCHLIST_KEY);
        if (savedWatchlist) {
            this.watchlist = JSON.parse(savedWatchlist);
            this.watchlistSubject.next(this.watchlist);
        } else {
            this.saveToLocalStorage(); // Save default watchlist if none exists
        }
    }

    // Save watchlist to local storage
    private saveToLocalStorage(): void {
        localStorage.setItem(this.WATCHLIST_KEY, JSON.stringify(this.watchlist));
    }

    // Get the single watchlist
    getWatchlist(): Observable<WatchListDetail> {
        return this.watchlistSubject.asObservable();
    }

    // Check if a movie is in the watchlist
    isInWatchlist(movieId: number): boolean {
        return this.watchlist.movies.some(m => m.movieId === movieId);
    }

    // Add a movie to the watchlist
    addToWatchlist(movie: Movie): Observable<void> {
        if (!this.isInWatchlist(movie.movieId)) {
            this.watchlist.movies.push(movie);
            this.saveToLocalStorage();
            this.watchlistSubject.next(this.watchlist);
        }
        return of(void 0);
    }

    // Remove a movie from the watchlist
    removeFromWatchlist(movieId: number): Observable<void> {
        this.watchlist.movies = this.watchlist.movies.filter(m => m.movieId !== movieId);
        this.saveToLocalStorage();
        this.watchlistSubject.next(this.watchlist);
        return of(void 0);
    }

    // Rename the watchlist
    renameWatchlist(name: string): Observable<void> {
        this.watchlist.name = name;
        this.saveToLocalStorage();
        this.watchlistSubject.next(this.watchlist);
        return of(void 0);
    }
}
