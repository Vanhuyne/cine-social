import { Component } from '@angular/core';
import { Movie, MovieResponse } from '../../../models/Movie';
import { MovieService } from '../../../service/movie.service';
import { combineLatest, Subscription } from 'rxjs';
import { SearchService } from '../../../service/search.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-movie-gird',
  templateUrl: './movie-gird.component.html',
  styleUrl: './movie-gird.component.scss'
})
export class MovieGirdComponent {
  movies: Movie[] = [];
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;
  loading = false;
  currentQuery: string = '';
  private subscriptions: Subscription[] = [];

  constructor(
    private movieService: MovieService,
    private searchService: SearchService,
    private route: ActivatedRoute,
    private router: Router
  ) {

  }

  ngOnInit() {
    // Combine route params and search service updates
    this.subscriptions.push(
      combineLatest([
        this.route.queryParams,
        this.searchService.searchQuery$,
        this.searchService.isSearchCleared$
      ]).subscribe(([params, searchQuery, isCleared]) => {
        if (isCleared) {
          this.loadInitialMovies();
          return;
        }

        const query = params['query'] || searchQuery;
        const page = params['page'] ? Number(params['page']) - 1 : 0;

        this.currentQuery = query;
        this.currentPage = page;

        if (query) {
          this.searchMovies(query, page);
        } else {
          this.loadInitialMovies();
        }

        if (query !== params['query'] || (page + 1) !== Number(params['page'])) {
          this.updateUrlParams(query, page + 1);
        }
      })
    );
  }

  loadInitialMovies() {
    this.loading = true;
    this.movieService.getMovies(0).subscribe({
      next: (response: MovieResponse) => {
        this.movies = response.content;
        this.totalPages = response.page.totalPages;
        this.totalElements = response.page.totalElements;
        this.pageSize = response.page.size;
        this.currentPage = 0;
        this.loading = false;

        // Update URL to remove search params
        this.updateUrlParams(null, 1);
      },
      error: (error) => {
        console.error('Error loading initial movies:', error);
        this.loading = false;
      }
    });
  }

  ngOnDestroy() {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  onPageChange(page: number) {
    this.updateUrlParams(this.currentQuery, page);
  }

  private updateUrlParams(query: string | null, page: number) {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        query: query || null,
        page: page
      },
      queryParamsHandling: 'merge'
    });
  }

  searchMovies(query: string, page: number = 0): void {
    this.loading = true;
    this.movieService.searchMovies(query, page).subscribe({
      next: (response: MovieResponse) => {
        this.movies = response.content;
        this.totalPages = response.page.totalPages;
        this.totalElements = response.page.totalElements;
        this.pageSize = response.page.size;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error fetching movies:', error);
        this.loading = false;
      }
    });
  }

  getMinValue(a: number, b: number): number {
    return Math.min(a, b);
  }

}
