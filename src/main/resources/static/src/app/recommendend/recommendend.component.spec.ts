import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecommendendComponent } from './recommendend.component';

describe('RecommendendComponent', () => {
  let component: RecommendendComponent;
  let fixture: ComponentFixture<RecommendendComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RecommendendComponent]
    });
    fixture = TestBed.createComponent(RecommendendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
