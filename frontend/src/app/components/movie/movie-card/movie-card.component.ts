import { Component, Input } from '@angular/core';
import { Movie } from '../../../models/Movie';

@Component({
  selector: 'app-movie-card',
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss'
})

export class MovieCardComponent {
 @Input() movie!: Movie;

 getImageUrl(path: string): string {
  return `https://image.tmdb.org/t/p/original${path}`;
}
}
