import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FAQsComponent } from './faqs.component';

describe('FAQsComponent', () => {
  let component: FAQsComponent;
  let fixture: ComponentFixture<FAQsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FAQsComponent]
    });
    fixture = TestBed.createComponent(FAQsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
