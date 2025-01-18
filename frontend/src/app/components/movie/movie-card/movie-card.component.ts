import { Component, Input } from '@angular/core';
import { Movie } from '../../../models/Movie';
import { Router } from '@angular/router';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { WatchlistService } from '../../../service/watchlist.service';

@Component({
  selector: 'app-movie-card',
  templateUrl: './movie-card.component.html',
  styleUrl: './movie-card.component.scss'
})

export class MovieCardComponent {
  @Input() movie!: Movie;
  showTrailer: boolean = false;
  safeTrailerUrl!: SafeResourceUrl;
  isInWatchlist: boolean = false;

  constructor(
    private router: Router,
    private sanitizer: DomSanitizer,
    private watchlistService: WatchlistService
  ) {}

  ngOnInit(): void {
    this.checkWatchlistStatus();
    
    // Subscribe to watchlist changes to update the button state
    this.watchlistService.getWatchlist().subscribe(() => {
      this.checkWatchlistStatus();
    });
  }

  getImageUrl(path: string): string {
    return `https://image.tmdb.org/t/p/original${path}`;
  }

  navigateToMovieDetails(): void {
    this.router.navigate(['/movie', this.movie.movieId]);
  }

  openTrailer(event: Event): void {
    event.stopPropagation();
    if (this.movie?.trailerKey) {
      this.safeTrailerUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
        `https://www.youtube.com/embed/${this.movie.trailerKey}?autoplay=1`
      );
      this.showTrailer = true;
    }
  }

  closeTrailer(event: Event): void {
    event.stopPropagation();
    this.showTrailer = false;
  }

  toggleWatchlist(event: Event): void {
    event.stopPropagation();
    if (this.isInWatchlist) {
      this.watchlistService.removeFromWatchlist(this.movie.movieId)
        .subscribe(() => {
          this.isInWatchlist = false;
        });
    } else {
      this.watchlistService.addToWatchlist(this.movie)
        .subscribe(() => {
          this.isInWatchlist = true;
        });
    }
  }

  private checkWatchlistStatus(): void {
    this.isInWatchlist = this.watchlistService.isInWatchlist(this.movie.movieId);
  }
}
