import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { LoginRequest } from '../models/auth/LoginRequest';
import { LoginResponse } from '../models/auth/LoginResponse';
import { TokenRefreshResponse } from '../models/auth/TokenRefreshResponse';
import { TokenRefreshRequest } from '../models/auth/TokenRefreshRequest';
import { jwtDecode  } from 'jwt-decode';
import { Router } from '@angular/router';
import { RegisterRequest } from '../models/auth/RegisterRequest';
import { RegisterResponse } from '../models/auth/RegisterResponse';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8081/api/auth';
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());
  public currentUser$ = new BehaviorSubject<string | null>(this.getUsernameFromToken());


  constructor(private http: HttpClient, private router: Router) {}
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials).pipe(
      tap((response: LoginResponse) => {
        localStorage.setItem('accessToken', response.accessToken);
        localStorage.setItem('refreshToken', response.refreshToken);
        this.loggedIn.next(true);
        this.currentUser$.next(this.getUsernameFromToken());
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
          localStorage.setItem('accessToken', response.accessToken);
          localStorage.setItem('refreshToken', response.refreshToken);
          this.currentUser$.next(this.getUsernameFromToken());
        }),
        catchError(() => {
          this.handleSessionExpiration();
          return throwError(() => new Error('Failed to refresh token'));
        })
      );
  }

  logout(): void {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    this.loggedIn.next(false);
    this.currentUser$.next(null);
    this.router.navigate(['/']);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }

  private hasToken(): boolean {
    return !!localStorage.getItem('accessToken');
  }

  private getUsernameFromToken(): string | null {
    const token = localStorage.getItem('accessToken');
    if (!token) return null;
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.sub || null;
    } catch (e) {
      return null;
    }
  }

  private handleSessionExpiration(): void {
    this.logout();
    alert('Session has expired. Please log in again.');
  }

}
