import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {
  showMovieList: boolean = true;
  showMovieGird: boolean = true;
  constructor(
    private router: ActivatedRoute,
  ) {}

  // check current route
  ngOnInit() {
    if (this.router.snapshot.url.map(segment => segment.path).includes('explore')) {
      this.showMovieList = false;
      this.showMovieGird = true
  }else{
    this.showMovieList = true;
    this.showMovieGird = false;
  }
}
}
