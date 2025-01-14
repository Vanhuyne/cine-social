import {  CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { inject } from '@angular/core';

export const AuthGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isLoggedIn()) {
    const requiredRoles = route.data['roles'] as string[];
    console.log('Required roles:', requiredRoles);
    if (requiredRoles && !authService.hasAnyRole(requiredRoles)) {
      router.navigate(['/unauthorized']);
      return false;
    }
    return true;
  } else {
    router.navigate(['/login']);
    return false;
  }
};
