import { TestBed, inject } from '@angular/core/testing';

import { SmokerserviceService } from './smokerservice.service';

describe('SmokerserviceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SmokerserviceService]
    });
  });

  it('should be created', inject([SmokerserviceService], (service: SmokerserviceService) => {
    expect(service).toBeTruthy();
  }));
});
