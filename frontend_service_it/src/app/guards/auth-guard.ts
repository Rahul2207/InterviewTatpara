import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from '../services/auth';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(Auth);
  const router = inject(Router);

  // 1. Check if user is logged in
  if (authService.isAuthenticated()) {
    return true; // Allow access
  }

  // 2. If not, redirect to Login page
  router.navigate(['/login']);
  return false; 
};
