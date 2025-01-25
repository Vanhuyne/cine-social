import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { LoginRequest } from '../models/auth/LoginRequest';
import { LoginResponse } from '../models/auth/LoginResponse';
import { TokenRefreshResponse } from '../models/auth/TokenRefreshResponse';
import { jwtDecode } from 'jwt-decode';
import { Router } from '@angular/router';
import { RegisterRequest } from '../models/auth/RegisterRequest';
import { RegisterResponse } from '../models/auth/RegisterResponse';

interface DecodedToken {
  sub: string;    // Subject (username)
  roles: string[]; // User roles
  exp: number;    // Expiration timestamp
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  private readonly apiUrl = 'http://localhost:8081/api/auth';

  private loggedIn = new BehaviorSubject<boolean>(this.hasValidToken());
  private currentUser$ = new BehaviorSubject<string | null>(this.getUsernameFromToken());
  private userRoles$ = new BehaviorSubject<string[]>(this.getUserRolesFromToken());

  constructor(private http: HttpClient, private router: Router) { }
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: LoginResponse) => {
        // Store tokens and update state
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);

        // Update logged in state and user information
        this.loggedIn.next(true);
        this.currentUser$.next(this.getUsernameFromToken());
        this.userRoles$.next(this.getUserRolesFromToken());
      })
    );
  }

  register(userRegister: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.apiUrl}/register`, userRegister);
  }

  refreshToken(): Observable<TokenRefreshResponse> {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) {
      this.logout();
      return throwError(() => new Error('Refresh token not available'));
    }

    return this.http
      .post<TokenRefreshResponse>(`${this.apiUrl}/refresh`, { refreshToken })
      .pipe(
        tap((response) => {
          // Store tokens and update state
          localStorage.setItem('accessToken', response.accessToken);
          localStorage.setItem('refreshToken', response.refreshToken);

          // Update logged in state and user information
          this.loggedIn.next(true);
          this.currentUser$.next(this.getUsernameFromToken());
        }),
        catchError(() => {
          this.handleSessionExpiration();
          return throwError(() => new Error('Failed to refresh token'));
        }));
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');

    this.loggedIn.next(false);
    this.currentUser$.next(null);
    this.userRoles$.next([]);
    this.router.navigate(['/']);
  }

  hasRole(role: string): boolean {
    const roles = this.getUserRolesFromToken();
    return roles.includes(role);
  }

  hasAnyRole(roles: string[]): boolean {
    const userRoles = this.getUserRolesFromToken();
    return roles.some(role => userRoles.includes(role));
  }

  private hasValidToken(): boolean {
    const token = localStorage.getItem('accessToken');
    if (!token) return false;
    try {
      const decodedToken = jwtDecode<DecodedToken>(token);
      const currentTime = Math.floor(Date.now() / 1000);
      return decodedToken.exp > currentTime;

    } catch (e) {
      return false;
    }
  }

  private getUsernameFromToken(): string | null {
    const token = localStorage.getItem('accessToken');
    if (!token) return null;

    try {
      const decodedToken = jwtDecode<DecodedToken>(token);
      return decodedToken.sub || null;
    } catch {
      return null;
    }
  }

  private getUserRolesFromToken(): string[] {
    const token = localStorage.getItem('accessToken');
    if (!token) return [];

    try {
      const decodedToken = jwtDecode<DecodedToken>(token);
      // console.log(decodedToken.roles);
      return decodedToken.roles || [];
    } catch {
      return [];
    }
  }

  // Public observables for components to subscribe to
  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  getCurrentUser(): Observable<string | null> {
    return this.currentUser$.asObservable();
  }

  getUserRoles(): Observable<string[]> {
    return this.userRoles$.asObservable();
  }

  private handleSessionExpiration(): void {
    this.logout();
    alert('Session has expired. Please log in again.');
  }

  redirectToGoogle() {
    const state = this.generateRandomString();
    localStorage.setItem('oauth_state', state);

    const params = new URLSearchParams({
      // client_id: environment.googleClientId,
      // redirect_uri: environment.googleRedirectUri,
      client_id: '365751882162-t0rc1qpigmbverunjccru6v374dhvlc2.apps.googleusercontent.com',
      redirect_uri: 'http://localhost:4200/login-callback',
      response_type: 'code',
      scope: 'openid email profile',
      state: state,
      access_type: 'offline'
    });
    
    window.location.href = `https://accounts.google.com/o/oauth2/v2/auth?${params.toString()}`;
  }

  private generateRandomString(): string {
    return Math.random().toString(36).substring(2, 15);
  }

  exchangeCodeForToken(code: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/google`, { code }).pipe(
      tap((response: LoginResponse) => {
        // Store tokens and update state
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);

        // Update logged in state and user information
        this.loggedIn.next(true);
        this.currentUser$.next(this.getUsernameFromToken());
        this.userRoles$.next(this.getUserRolesFromToken());
      })
    );;
  }
}
