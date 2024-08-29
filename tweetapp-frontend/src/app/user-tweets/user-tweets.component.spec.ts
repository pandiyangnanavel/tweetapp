import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from "@angular/router/testing";

import { UserTweetsComponent } from './user-tweets.component';

describe('UserTweetsComponent', () => {
  let component: UserTweetsComponent;
  let fixture: ComponentFixture<UserTweetsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserTweetsComponent],
      imports: [RouterTestingModule,
      HttpClientTestingModule]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserTweetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
