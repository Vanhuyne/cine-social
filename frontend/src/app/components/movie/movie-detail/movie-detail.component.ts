import { Component } from '@angular/core';
import { Movie } from '../../../models/Movie';
import { MovieService } from '../../../service/movie.service';
import { ActivatedRoute } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-movie-detail',
  templateUrl: './movie-detail.component.html',
  styleUrl: './movie-detail.component.scss'
})
export class MovieDetailComponent {
  movie : Movie | null = null;
  loading:  boolean = true;
  error : string = '';
  trailerKey: string = 'QzQynLEyzKA';
  showTrailer: boolean = false;
  safeTrailerUrl!: SafeResourceUrl;

  constructor(
    private movieService: MovieService,
    private route: ActivatedRoute,
    private sanitizer: DomSanitizer
  ) {
    
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const movieId = params['movieId'];
      if (movieId) {
        this.loadMovieDetails(movieId);
      }
    });
  }
  loadMovieDetails(movieId:  number): void {
    this.movieService.getMovieById(movieId).subscribe({
      next: (movie) => {
        this.movie = movie;
        console.log(movie);
        
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Failed to load movie details';
        this.loading = false;
      }
    });
   
  }

  getImageUrl(path: string): string {
    return `https://image.tmdb.org/t/p/original/${path}`;
    // https://image.tmdb.org/t/p/original//zQKFPx03M9RKfEcRETXR2HoUZn1.jpg
  }

  formatRuntime(minutes: number): string {
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;
    return `${hours}h ${remainingMinutes}m`;
  }
  openTrailer(): void {
    if (this.trailerKey) {
      this.safeTrailerUrl = this.sanitizer.bypassSecurityTrustResourceUrl(`https://www.youtube.com/embed/${this.trailerKey}?autoplay=1`);
      this.showTrailer = true;
    }
  }

  closeTrailer(): void {
    this.showTrailer = false;
  }
}
