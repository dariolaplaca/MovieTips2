import { TestBed } from '@angular/core/testing';

import { RentalOrderService } from './rental-order.service';

describe('RentalOrderService', () => {
  let service: RentalOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RentalOrderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
