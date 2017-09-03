import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GrafiekComponent } from './grafiek.component';

describe('GrafiekComponent', () => {
  let component: GrafiekComponent;
  let fixture: ComponentFixture<GrafiekComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GrafiekComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GrafiekComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
