import { Component } from '@angular/core';
import { MovieService } from '../../service/movie.service';
import { Movie } from '../../models/Movie';

@Component({
  selector: 'app-hero',
  templateUrl: './hero.component.html',
  styleUrl: './hero.component.scss'
})
export class HeroComponent {
  movies: Movie[] = [];
  constructor(
    private movieService: MovieService
  ) {
    this.movieService.getMoviesByPupularity(0).subscribe((response) => {
      // get movie with 0 index
      this.movies = response.content.slice(0, 1);
      console.log(this.movies);
    });
   }

  getImageUrl(path: string): string {
    return `https://image.tmdb.org/t/p/original/${path}`;
  }
}
