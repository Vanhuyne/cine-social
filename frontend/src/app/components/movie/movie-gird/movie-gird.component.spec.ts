import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MovieGirdComponent } from './movie-gird.component';

describe('MovieGirdComponent', () => {
  let component: MovieGirdComponent;
  let fixture: ComponentFixture<MovieGirdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MovieGirdComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MovieGirdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
