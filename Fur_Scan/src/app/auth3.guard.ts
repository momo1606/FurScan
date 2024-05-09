import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

export const auth3Guard: CanActivateFn = (route, state) => {
  const router = inject(Router)

  if (localStorage.getItem('petData')) {
    return true;
  } else {
    router.navigate(['']);
    return false;
  }
};
