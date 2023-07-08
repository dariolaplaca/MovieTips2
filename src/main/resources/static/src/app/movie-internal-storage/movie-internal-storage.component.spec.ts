import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieInternalStorageComponent } from './movie-internal-storage.component';

describe('MovieInternalStorageComponent', () => {
  let component: MovieInternalStorageComponent;
  let fixture: ComponentFixture<MovieInternalStorageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MovieInternalStorageComponent]
    });
    fixture = TestBed.createComponent(MovieInternalStorageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
