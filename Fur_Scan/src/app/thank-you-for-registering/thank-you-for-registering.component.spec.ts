import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ThankYouForRegisteringComponent } from './thank-you-for-registering.component';

describe('ThankYouForRegisteringComponent', () => {
  let component: ThankYouForRegisteringComponent;
  let fixture: ComponentFixture<ThankYouForRegisteringComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ThankYouForRegisteringComponent]
    });
    fixture = TestBed.createComponent(ThankYouForRegisteringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
