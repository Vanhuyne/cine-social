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

  // check login 
  watchList: WatchList[] = [];
  selectedWatchList!: WatchListDetail;
  constructor(
    private watchlistService: WatchlistService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.watchlistService.getAllWatchlist().subscribe(data => {
      this.watchList = data;
      console.log(this.watchList);
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
  
}
