import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, BehaviorSubject, throwError } from 'rxjs';
import { catchError, switchMap, filter, take } from 'rxjs/operators';
import { AuthService } from '../service/auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  constructor(private authService: AuthService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    // Bước 1: Kiểm tra URL của request
    if (request.url.includes('/api/auth/login') ||
      request.url.includes('/api/auth/register') ||
      request.url.includes('/api/auth/refresh')) {
      return next.handle(request);
    }

    // Bước 2: Thêm token vào header nếu có
    const token = localStorage.getItem('accessToken');
    if (token) {
      request = this.addToken(request, token);
    }

    // Bước 3: Xử lý response và bắt lỗi 401
    return next.handle(request).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return this.handle401Error(request, next);
        }
        return throwError(() => error);
      })
    );
  }

  private addToken(request: HttpRequest<any>, token: string): HttpRequest<any> {
    return request.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
  }


  private handle401Error(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);

      return this.authService.refreshToken().pipe(
        switchMap((token: any) => {
          // Bước 1: Refresh thành công
          this.isRefreshing = false;
          this.refreshTokenSubject.next(token.accessToken);
          // Bước 2: Retry request cũ với token mới
          return next.handle(this.addToken(request, token.accessToken));
        }),
        catchError((err) => {
          // Bước 3: Refresh thất bại
          this.isRefreshing = false;
          this.authService.logout();
          return throwError(() => err);
        })
      );
    }

    // CASE 2: Đang trong quá trình refresh
    return this.refreshTokenSubject.pipe(
      // Bước 1: Đợi cho đến khi có token mới (filter null)
      filter(token => token != null),
      // Bước 2: Chỉ lấy giá trị đầu tiên
      take(1),
      // Bước 3: Retry request với token mới
      switchMap(token => {
        return next.handle(this.addToken(request, token));
      })
    );
  }
}