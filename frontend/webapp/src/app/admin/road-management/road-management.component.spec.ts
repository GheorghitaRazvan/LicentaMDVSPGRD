import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoadManagementComponent } from './road-management.component';

describe('RoadManagementComponent', () => {
  let component: RoadManagementComponent;
  let fixture: ComponentFixture<RoadManagementComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RoadManagementComponent]
    });
    fixture = TestBed.createComponent(RoadManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
