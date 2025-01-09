import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, switchMap, filter, take, finalize } from 'rxjs/operators';
import { AuthService } from '../service/auth.service';
import { TokenRefreshResponse } from '../models/auth/TokenRefreshResponse';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  // URLs that don't need authentication
  private readonly whitelistedUrls = [
    '/api/auth/login',
    '/api/auth/register',
    '/api/auth/refresh'
  ];

  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Skip token for whitelisted URLs
    if (this.isWhitelistedUrl(req.url)) {
      return next.handle(req);
    }

    const authToken = this.authService.getAuthToken();
    if (authToken) {
      req = this.addTokenHeader(req, authToken);
    }

    return next.handle(req).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse) {
          if (error.status === 401) {
            return this.handle401Error(req, next);
          } else if (error.status === 403) {
            // Handle forbidden error
            this.authService.logout();
            return throwError(() => error);
          }
        }
        return throwError(() => error);
      })
    );

  }

  private addTokenHeader(request: HttpRequest<any>, token: string) {
    return request.clone({
      headers: request.headers.set('Authorization', `Bearer ${token}`)
    });
  }

  private isWhitelistedUrl(url: string): boolean {
    return this.whitelistedUrls.some(whitelistedUrl => 
      url.includes(whitelistedUrl)
    );
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      const refreshToken = this.authService.getRefreshToken();
      
      if (!refreshToken) {
        this.isRefreshing = false;
        this.authService.logout();
        return throwError(() => 'Refresh token not found');
      }

      return this.authService.refreshToken(refreshToken).pipe(
        switchMap((response: TokenRefreshResponse) => {
          this.refreshTokenSubject.next(response.accessToken);
          return next.handle(this.addTokenHeader(request, response.accessToken));
        }),
        catchError((error) => {
          this.authService.logout();
          return throwError(() => error);
        }),
        finalize(() => {
          this.isRefreshing = false;
        })
      );
    }

    return this.refreshTokenSubject.pipe(
      filter(token => token !== null),
      take(1),
      switchMap(token => next.handle(this.addTokenHeader(request, token)))
    );
  }
}