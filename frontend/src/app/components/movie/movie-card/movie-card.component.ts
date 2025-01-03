import { Component, Input } from '@angular/core';
import { Movie } from '../../../models/Movie';
import { Router } from '@angular/router';

@Component({
  selector: 'app-movie-card',
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss'
})

export class MovieCardComponent {
  @Input() movie!: Movie;
  constructor(
    private router: Router
  ) { }
  getImageUrl(path: string): string {
    return `https://image.tmdb.org/t/p/original${path}`;
  }
  navigateToMovieDetails(): void {
    this.router.navigate(['/movie', this.movie.movieId]);
  }

}
