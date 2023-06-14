import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRoadComponent } from './add-road.component';

describe('AddRoadComponent', () => {
  let component: AddRoadComponent;
  let fixture: ComponentFixture<AddRoadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddRoadComponent]
    });
    fixture = TestBed.createComponent(AddRoadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
