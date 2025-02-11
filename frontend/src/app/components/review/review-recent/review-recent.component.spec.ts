import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewRecentComponent } from './review-recent.component';

describe('ReviewRecentComponent', () => {
  let component: ReviewRecentComponent;
  let fixture: ComponentFixture<ReviewRecentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReviewRecentComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReviewRecentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
