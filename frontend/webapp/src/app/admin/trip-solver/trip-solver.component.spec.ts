import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TripSolverComponent } from './trip-solver.component';

describe('TripSolverComponent', () => {
  let component: TripSolverComponent;
  let fixture: ComponentFixture<TripSolverComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TripSolverComponent]
    });
    fixture = TestBed.createComponent(TripSolverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
