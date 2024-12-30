import { Component } from '@angular/core';
import { Movie, MovieResponse } from '../../../models/Movie';
import { MovieService } from '../../../service/movie.service';

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

  constructor(private movieService: MovieService) {
    
  }

  ngOnInit() {
    this.searchMovies('');
  }

  onSearch(query: string): void {
    this.currentQuery = query;
    this.currentPage = 0; // Reset to the first page for a new search
    this.searchMovies(query);
  }

  loadMovies(page: number) {
    this.loading = true;
    this.movieService.getMovies(page).subscribe({
      next: (response: MovieResponse) => {
        this.movies = response.content;
        this.currentPage = response.page.number;
        this.pageSize = response.page.size;
        this.totalPages = response.page.totalPages;
        this.totalElements = response.page.totalElements;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading movies:', error);
        this.loading = false;
      }
    });
  }

  onPageChange(page: number) {
    this.currentPage = page - 1; 
    this.searchMovies(this.currentQuery, this.currentPage); // Convert to 0-based for backend
    const element = document.getElementById('movie-gird');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  getMinValue(a: number, b: number): number {
    return Math.min(a, b);
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
  

}
