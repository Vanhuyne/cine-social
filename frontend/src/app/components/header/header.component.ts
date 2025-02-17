import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../service/auth.service';
import {  map, Observable, Subscription } from 'rxjs';
import { UserService } from '../../service/user.service';
import { UserProfile } from '../../models/User';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit ,OnDestroy {
  currentUser: UserProfile | null = null;
  isLoggedIn$ = this.authService.isLoggedIn();
  isAdmin$ = this.authService.getUserRoles().pipe(
    map(roles => roles.includes('ROLE_ADMIN'))
  );
  
  isDropdownOpen = false;

  private userSubscription?: Subscription;
  private authSubscription?: Subscription;

  constructor(
    private router: Router,
    public authService: AuthService,
    private userService: UserService
  ) {
    
  }

  ngOnInit(): void {
    // Subscribe to auth state changes
    this.authSubscription = this.authService.isLoggedIn().subscribe(isLoggedIn => {
      if (isLoggedIn) {
        // Only fetch user profile when logged in
        this.loadUserProfile();
      } else {
        this.currentUser = null;
      }
    });
  }
  private loadUserProfile(): void {
    this.userSubscription = this.userService.getCurrentUserProfile().subscribe({
      next: (user) => {
        this.currentUser = user;
      },
      error: (err) => {
        console.error('Error fetching current user details:', err);
      },
    });
  }

  getProfileImageUrl(fileName: string | undefined): string {
    if (!fileName) return '';
    return `${environment.apiUrl}/users/image/${fileName}`;
  }

  ngOnDestroy() {
    this.userSubscription?.unsubscribe();
    this.authSubscription?.unsubscribe();
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
