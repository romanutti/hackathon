import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import { SearchComponent } from './search.component';
import {UserInfoDto} from "../../model/UserInfoDto";
import {AppService} from "../app.service";
import {of, throwError} from "rxjs";
import {SearchError} from "../../model/search/SearchError";

describe('SearchComponent', () => {
  let component: SearchComponent;
  let fixture: ComponentFixture<SearchComponent>;
  let appServiceSpy = jasmine.createSpyObj('AppService', [
    'getUserInfo',
  ]);

  const userId = 'lewg';
  const userInfoData: UserInfoDto = {
    id: userId,
    firstName: 'Lewin',
    lastName: 'Gerber',
    jobTitle: 'Trainee',
    pictureId: '0',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SearchComponent ],
      providers: [{ provide: AppService, useValue: appServiceSpy }],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  beforeEach(() => {
    appServiceSpy.getUserInfo.and.returnValue(of(userInfoData));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('get userinfo', () => {
    it('should retrieve user info when search term is entered', () => {
      spyOn(component.foundUser, 'emit');

      component.searchForTerm("lewg");
      fixture.detectChanges();

      expect(component.foundUser.emit).toHaveBeenCalledWith('lewg');
    });

    it('should catch error if the user is not found', () => {
      const httpError: SearchError = {searchError: 504}
      appServiceSpy.getUserInfo.and.returnValue(throwError({ status: 504 }));

      component.searchForTerm("test");
      fixture.detectChanges();

      console.log(component.httpError)
      expect(component.httpError).toEqual(httpError);
    });

    it('should not emit when user is not found', () => {
      spyOn(component.foundUser, 'emit');
      appServiceSpy.getUserInfo.and.returnValue(throwError({ status: 504 }));

      component.searchForTerm("test");
      fixture.detectChanges();

      expect(component.foundUser.emit).not.toHaveBeenCalled();
    });
  });

  describe('input change handling', () => {
    it('should unsubscribe change observable on destroy', () => {
      component.search.setValue({searchTerm: "term"});
      component.ngOnDestroy();

      // @ts-ignore
      expect(component.onInputChange$.closed).toEqual(true);
    });

    it("should not update the search term", () => {
      component.debounceMilliseconds = 2000;
      fixture.detectChanges();
      spyOn(component.foundUser, 'emit');

      component.search.setValue({searchTerm: "term"});
      fixture.detectChanges();

      expect(component.foundUser.emit).not.toHaveBeenCalled();
    });

    it('should update the search term after 500ms', fakeAsync(() => {
      component.debounceMilliseconds = 500;
      fixture.detectChanges();
      spyOn(component.foundUser, 'emit');

      component.search.setValue({searchTerm: "term"});
      fixture.detectChanges();
      tick(500);

      expect(component.foundUser.emit).toHaveBeenCalled();
    }));
  });
});
