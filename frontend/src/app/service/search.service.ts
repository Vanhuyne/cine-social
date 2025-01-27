import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {
  private searchQuerySubject = new BehaviorSubject<string>('');
  private isSearchClearedSubject = new BehaviorSubject<boolean>(false);
  
  searchQuery$ = this.searchQuerySubject.asObservable();
  isSearchCleared$ = this.isSearchClearedSubject.asObservable();

  updateSearchQuery(query: string) {
    this.searchQuerySubject.next(query);
    this.isSearchClearedSubject.next(false);
  }

  clearSearch() {
    this.searchQuerySubject.next('');
    this.isSearchClearedSubject.next(true);
  }
}
