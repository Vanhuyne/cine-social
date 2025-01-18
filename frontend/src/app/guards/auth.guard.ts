import {  CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { inject } from '@angular/core';
import { map } from 'rxjs';

export const AuthGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.isLoggedIn().pipe(
    map(isLoggedIn => {
      if (isLoggedIn) {
        const requiredRoles = route.data['roles'] as string[];
        console.log('Required roles:', requiredRoles);
        
        if (requiredRoles && !authService.hasAnyRole(requiredRoles)) {
          router.navigate(['/unauthorized']);
          return false;
        }
        return true;
      } else {
        router.navigate(['/']);
        alert('You need to login to access this page');
        return false;
      }
    })
  );
};

// Kiểm tra trạng thái đăng nhập
// Nếu đã đăng nhập, kiểm tra roles
// Nếu có roles phù hợp, cho phép truy cập
// Nếu không có roles phù hợp, chuyển đến trang unauthorized
// Nếu chưa đăng nhập, chuyển về trang chủ
