import { Component, ElementRef, ViewChild } from '@angular/core';
import { Movie, MovieResponse } from '../../../models/Movie';
import { MovieService } from '../../../service/movie.service';

@Component({
  selector: 'app-movie-list',
  templateUrl: './movie-list.component.html',
  styleUrl: './movie-list.component.scss'
})
export class MovieListComponent {
  @ViewChild('movieContainer') movieContainer!: ElementRef;
  movies: Movie[] = []
  
  constructor(
    private movieService: MovieService
  ) {
    this.movieService.getMoviesByPupularity(0

    ).subscribe((response : MovieResponse) => {
      this.movies = response.content;
    })
  }

  getImageUrl(path: string): string {
    return `https://image.tmdb.org/t/p/original${path}`;
  }

  scrollRight(): void {
    const container = this.movieContainer.nativeElement;
    // Get first movie item
    const firstItem = container.querySelector('.flex-none');
    if (firstItem) {
      // Calculate total width of one item (including gap)
      const itemWidth = firstItem.offsetWidth + 24; // 24px is the gap-6 (1.5rem = 24px)
      container.scrollTo({
        left: container.scrollLeft + itemWidth,
        behavior: 'smooth'
      });
    }
  }

  scrollLeft() {
    const container = this.movieContainer.nativeElement;
    const firstItem = container.querySelector('.flex-none');
    if (firstItem) {
      // Calculate total width of one item (including gap)
      const itemWidth = firstItem.offsetWidth + 24; // 24px is the gap-6 (1.5rem = 24px)
      container.scrollTo({
        left: container.scrollLeft - itemWidth,
        behavior: 'smooth'
      });
    }
  }
}
