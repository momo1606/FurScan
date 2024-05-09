import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { authThankYouPageGuard } from './auth-thank-you-page.guard';

describe('authThankYouPageGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => authThankYouPageGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
