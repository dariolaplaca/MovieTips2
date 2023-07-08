import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RentalOrderComponent } from './rental-order.component';

describe('RentalOrderComponent', () => {
  let component: RentalOrderComponent;
  let fixture: ComponentFixture<RentalOrderComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RentalOrderComponent]
    });
    fixture = TestBed.createComponent(RentalOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
