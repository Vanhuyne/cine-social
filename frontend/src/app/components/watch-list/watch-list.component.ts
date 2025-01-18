import { Component } from '@angular/core';

import { WatchListDetail } from '../../models/WatchListDetail';
import { WatchlistService } from '../../service/watchlist.service';


@Component({
  selector: 'app-watch-list',
  templateUrl: './watch-list.component.html',
  styleUrl: './watch-list.component.scss'
})
export class WatchListComponent {
    watchlist!: WatchListDetail | null;

    constructor(private watchlistService: WatchlistService) {}
  
    ngOnInit(): void {
      // Subscribe to watchlist changes
      this.watchlistService.getWatchlist().subscribe((watchlist) => {
        this.watchlist = watchlist;
      });
    }
}
