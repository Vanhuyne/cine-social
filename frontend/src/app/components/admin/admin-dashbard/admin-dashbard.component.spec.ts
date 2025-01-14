import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminDashbardComponent } from './admin-dashbard.component';

describe('AdminDashbardComponent', () => {
  let component: AdminDashbardComponent;
  let fixture: ComponentFixture<AdminDashbardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminDashbardComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AdminDashbardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
