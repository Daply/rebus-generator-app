import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InfoComponent } from './info.component';

describe('InfoComponent', () => {
  let component: InfoComponent;
  let fixture: ComponentFixture<InfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe(':', () => {

    function setup() {
      const fixture = TestBed.createComponent(InfoComponent);
      const app = fixture.debugElement.componentInstance;
      return { fixture, app };
    }

    it('should create the app', async(() => {
      const { app } = setup();
      expect(app).toBeTruthy();
    }));

    it('should have home variable as \'Back to rebus generator\'', async(() => {
      const { app } = setup();
      expect(app.home).toBe('Back to rebus generator');
    }));

    it('should have p tag as \'Back to rebus generator\'', async(() => {
      const { app, fixture } = setup();
      fixture.detectChanges();
      const compile = fixture.debugElement.nativeElement;
      const ptag = compile.querySelector('p');
      expect(ptag.textContent).toBe('Back to rebus generator');
    }));
  });
});
