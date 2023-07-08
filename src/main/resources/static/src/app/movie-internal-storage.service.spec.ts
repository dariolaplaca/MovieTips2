import { TestBed } from '@angular/core/testing';

import { MovieInternalStorageService } from './movie-internal-storage.service';

describe('MovieInternaStorageService', () => {
  let service: MovieInternalStorageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MovieInternalStorageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
