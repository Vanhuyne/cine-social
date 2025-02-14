import { Component, HostListener, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import {  map, Observable, Subscription } from 'rxjs';
import { UserService } from '../../service/user.service';
import { UserProfile } from '../../models/User';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnDestroy {
  currentUser: UserProfile | null = null;
  isLoggedIn$ = this.authService.isLoggedIn();
  isAdmin$ = this.authService.getUserRoles().pipe(
    map(roles => roles.includes('ROLE_ADMIN'))
  );
  
  isDropdownOpen = false;

  private userSubscription?: Subscription;

  constructor(
    private router: Router,
    public authService: AuthService,
    private userService: UserService
  ) {
    // this.authSubscription = this.authService.getCurrentUser().subscribe();
  }

  ngOnInit(): void {
    // Subscribe to get the full user details (including profilePicture)
    this.userSubscription = this.userService.getCurrentUserProfile().subscribe({
      next: (user) => {
        this.currentUser = user;
      },
      error: (err) => {
        console.error('Error fetching current user details:', err);
      },
    });
  }

  getProfileImageUrl(fileName: string): string {
    return this.userService.getProfileImageUrl(fileName);
  }

  ngOnDestroy() {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
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

  
}
