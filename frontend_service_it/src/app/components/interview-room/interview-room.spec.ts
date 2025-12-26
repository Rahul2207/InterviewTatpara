import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InterviewRoom } from './interview-room';

describe('InterviewRoom', () => {
  let component: InterviewRoom;
  let fixture: ComponentFixture<InterviewRoom>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InterviewRoom]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InterviewRoom);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
