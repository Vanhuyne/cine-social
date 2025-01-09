import { Component } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  showLoginModal = false;
  username: string | null = null;

  constructor(
    private router: Router
  ) {}

  handleLoginSuccess(username: string) {
    this.username = username;
    this.showLoginModal = false;
  }
}
