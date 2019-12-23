import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RebusComponent } from './rebus.component';

describe('RebusComponent', () => {
  let component: RebusComponent;
  let fixture: ComponentFixture<RebusComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RebusComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RebusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
