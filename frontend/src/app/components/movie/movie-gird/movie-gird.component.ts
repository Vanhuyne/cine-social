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

  constructor(private movieService: MovieService) {}

  ngOnInit() {
    this.loadMovies(this.currentPage);
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
    this.loadMovies(page - 1); // Convert to 0-based for backend
    const element = document.getElementById('movie-gird');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
    }
  }

  getMinValue(a: number, b: number): number {
    return Math.min(a, b);
  }
}
