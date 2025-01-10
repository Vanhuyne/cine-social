import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../service/auth.service';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  showLoginModal = false;
  isDropdownOpen = false;
  private authSubscription?: Subscription;

  constructor(
    private router: Router,
    public authService: AuthService
  ) {
    this.authSubscription = this.authService.currentUser$.subscribe(user => {
      // You can add additional logic here when the user state changes
      if (!user) {
        this.showLoginModal = false;
        this.isDropdownOpen = false;
      }
    });
  }

  ngOnDestroy() {
    // Clean up subscription
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
  toggleDropdown() {
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown() {
    this.isDropdownOpen = false;
  }

  logout() {
    this.authService.logout();
    this.closeDropdown();
    // this.router.navigate(['/']);
  }
}
