import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const authThankYouPageGuard: CanActivateFn = (route, state) => {
  const router = inject(Router) // Create an instance of Router
  if (localStorage.getItem('thankYouPage')) {
    return true;
  } else {
    router.navigate(['']);
    return false;
  }
};
