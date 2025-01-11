import { Component, HostListener, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../../service/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnDestroy {
  showLoginModal = false;
  showRegisterModal = false;
  
  isDropdownOpen = false;

  private authSubscription?: Subscription;

  constructor(
    private router: Router,
    public authService: AuthService
  ) {
    this.authSubscription = this.authService.currentUser$.subscribe(user => {
      if (!user) {
        this.showLoginModal = false;
        this.isDropdownOpen = false;
      }
    });
  }

  ngOnDestroy() {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  toggleDropdown(event: Event) {
    event.stopPropagation();
    this.isDropdownOpen = !this.isDropdownOpen;
  }

  closeDropdown() {
    this.isDropdownOpen = false;
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: Event) {
    const target = event.target as HTMLElement;
    const dropdown = document.querySelector('.dropdown-menu');
    if (dropdown && !dropdown.contains(target)) {
      this.closeDropdown();
    }
  }

  logout() {
    this.authService.logout();
    this.closeDropdown();
  }

  // Open Login Modal
  openLoginModal(): void {
    this.showLoginModal = true;
    this.showRegisterModal = false;
  }

  // Open Register Modal
  openRegisterModal(): void {
    this.showRegisterModal = true;
    this.showLoginModal = false;
  }

  // Close Modals
  closeLoginModal(): void {
    this.showLoginModal = false;
  }

  closeRegisterModal(): void {
    this.showRegisterModal = false;
  }

  // Switch to Register Modal
  switchToRegister(): void {
    this.openRegisterModal();
  }

  // Switch to Login Modal
  switchToLogin(): void {
    this.openLoginModal();
  }
}
