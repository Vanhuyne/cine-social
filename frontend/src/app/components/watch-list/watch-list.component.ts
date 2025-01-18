import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { WatchList } from '../../models/WatchList';
import { WatchlistService } from '../../service/watchlist.service';
import { WatchListDetail } from '../../models/WatchListDetail';


@Component({
  selector: 'app-watch-list',
  templateUrl: './watch-list.component.html',
  styleUrl: './watch-list.component.scss'
})
export class WatchListComponent {

  showCreateDialog = false;
  newListName = '';
  isCreating = false;

  watchList: WatchList[] = [];
  selectedWatchList!: WatchListDetail;
  isLoading = false;
  constructor(
    private watchlistService: WatchlistService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadWatchlists();
  }

  private loadWatchlists(): void {
    this.watchlistService.getAllWatchlist().subscribe(data => {
      this.watchList = data;
      if (this.watchList.length > 0) {
        this.showMovies(this.watchList[0]);
      }
    });
  }

  showMovies(item: WatchList) {
    this.watchlistService.getWatchListDedail(item.watchlistId).subscribe(data => {
      this.selectedWatchList = data;
      console.log(this.selectedWatchList);
    });
  }
  getImageUrl(path: string): string {
    return `https://image.tmdb.org/t/p/original${path}`;
  }

  removeItem(item: WatchList) {
    this.isLoading = true;
    this.watchlistService.removeWatchList(item.watchlistId).subscribe({
      next: () => {
        this.watchList = this.watchList.filter(x => x.watchlistId !== item.watchlistId);

        // If we removed the currently selected watchlist
        if (this.selectedWatchList?.watchlistId === item.watchlistId) {
          if (this.watchList.length > 0) {
            this.showMovies(this.watchList[0]);
          } else {
            // No watchlists left
            this.selectedWatchList = null as any;
          }
        }
      },
      error: (err) => {
        console.log("Error removing watchlist", err);
      }
    });
  }

  closeCreateDialog() {
    this.showCreateDialog = false;
    this.isCreating = false;
  }
  createWatchlist() {
    if (!this.newListName.trim()) {
      return;
    }

    const formattedName = this.newListName.trim().toLowerCase().replace(/\s+/g, '-');
    this.isCreating = true;
    this.watchlistService.createWatchlist(formattedName).subscribe({
      next: (newWatchlist) => {
        this.watchList = [...this.watchList, newWatchlist];
        this.closeCreateDialog();
        this.showMovies(newWatchlist);
      },
      error: (err) => {
        console.log("Error creating watchlist", err);
        this.isCreating = false;
      }
    });
  }
  openCreateDialog(): void {
    this.showCreateDialog = true;
    this.newListName = '';
  }
}
