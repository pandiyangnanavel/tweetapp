import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { TweetsDataService } from './tweets-data.service';

describe('TweetsDataService', () => {
  let service: TweetsDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[HttpClientTestingModule]
    });
    service = TestBed.inject(TweetsDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
