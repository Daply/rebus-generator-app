import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminComponent } from './admin.component';
import { RebusService } from '../rebus/rebus.service';

describe('AdminComponent', () => {
  let component: AdminComponent;
  let fixture: ComponentFixture<AdminComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdminComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe(':', () => {

    function setup() {
      const fixture = TestBed.createComponent(AdminComponent);
      const app = fixture.debugElement.componentInstance;
      const rebusService = fixture.debugElement.injector.get(RebusService);
      return { fixture, app, rebusService };
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
