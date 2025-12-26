import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Pofile } from './pofile';

describe('Pofile', () => {
  let component: Pofile;
  let fixture: ComponentFixture<Pofile>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Pofile]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Pofile);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
