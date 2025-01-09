import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { LoginRequest } from '../models/auth/LoginRequest';
import { LoginResponse } from '../models/auth/LoginResponse';
import { TokenRefreshResponse } from '../models/auth/TokenRefreshResponse';
import { TokenRefreshRequest } from '../models/auth/TokenRefreshRequest';
import { jwtDecode  } from 'jwt-decode';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = 'http://localhost:8081/api/auth';
  private readonly TOKEN_KEY = 'accessToken';
  private readonly REFRESH_TOKEN_KEY = 'refreshToken';
  private readonly STORAGE_TYPE = 'localStorage'; 

  constructor(private http: HttpClient,
    private router: Router
  ) { }

  login(value: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, value).pipe(
      tap((response: LoginResponse) => {
        this.saveTokens(response.accessToken, response.refreshToken);
      })
    );
  }

  refreshToken(refreshToken: string): Observable<TokenRefreshResponse> {
    const request: TokenRefreshRequest = { refreshToken };
    return this.http.post<TokenRefreshResponse>(`${this.apiUrl}/refresh`, request).pipe(
      tap((response: TokenRefreshResponse) => {
        this.saveAccessToken(response.accessToken);
        // Save new refresh token if server returns it
        if (response.refreshToken) {
          this.saveRefreshToken(response.refreshToken);
        }
      })
    );
  }

  private getStorage(): Storage {
    return this.STORAGE_TYPE === 'localStorage' ? localStorage : sessionStorage;
  }

  private saveTokens(accessToken: string, refreshToken: string): void {
    const storage = this.getStorage();
    storage.setItem(this.TOKEN_KEY, accessToken);
    storage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
  }

  private saveAccessToken(accessToken: string): void {
    this.getStorage().setItem(this.TOKEN_KEY, accessToken);
  }

  private saveRefreshToken(refreshToken: string): void {
    this.getStorage().setItem(this.REFRESH_TOKEN_KEY, refreshToken);
  }

  getAuthToken(): string | null {
    return this.getStorage().getItem(this.TOKEN_KEY);
  }

  getRefreshToken(): string | null {
    return this.getStorage().getItem(this.REFRESH_TOKEN_KEY);
  }

  isLoggedIn(): boolean {
    const token = this.getAuthToken();
    return !!token && !this.isTokenExpired(token);
  }

  private isTokenExpired(token: string): boolean {
    try {
      const decoded: any = jwtDecode(token);
      if (!decoded.exp) return true;
      
      // Thêm buffer 30 giây để tránh edge cases
      return decoded.exp * 1000 <= Date.now() + 30000;
    } catch {
      return true;
    }
  }

  private checkTokenExpiration(): void {
    const token = this.getAuthToken();
    if (token && this.isTokenExpired(token)) {
      const refreshToken = this.getRefreshToken();
      if (!refreshToken) {
        this.logout();
        return;
      }

      this.refreshToken(refreshToken).subscribe({
        error: () => this.logout()
      });
    }
  }

  logout(): void {
    this.getStorage().clear();
    this.router.navigate(['/movies']);
  }


}
