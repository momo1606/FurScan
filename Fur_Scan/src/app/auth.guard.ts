import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router) // Create an instance of Router
  
  if (!localStorage.getItem('token')) {
    return true;
  } else {
    let data = localStorage.getItem('userPersonalData');
    if (data !== null) {
      let jsonData = JSON.parse(data);
      if (jsonData.is_doctor === 1) {
        router.navigateByUrl('/doctorDashboard');
      } else {
        router.navigateByUrl('/dashboard');
      }
    } else {
      router.navigateByUrl('');
    }
    return false;
  }
};
