import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { BadgeDto } from 'src/model/BadgeDto';
import { SkillDto } from 'src/model/SkillDto';
import { UserInfoDto } from 'src/model/UserInfoDto';
import { AppService } from '../app.service';

import { UserComponent } from './user.component';

describe('UserComponent', () => {
  let component: UserComponent;
  let fixture: ComponentFixture<UserComponent>;
  let appServiceSpy = jasmine.createSpyObj('AppService', [
    'getUserInfo',
    'getBadges',
    'getSkills',
  ]);

  const userId = 'mrom';
  const userInfoData: UserInfoDto = {
    id: userId,
    firstName: 'Marco',
    lastName: 'Romanutti',
    jobTitle: 'Advanced Software Engineer',
    pictureId: '23320',
  };
  const badgesData: BadgeDto[] = [
    {
      id: 'hackathon',
      description: 'Zuehlke Hackathony',
    },
  ];
  const skillsData: SkillDto[] = [
    {
      skillId: 89,
      name: 'JavaScript',
      description:
        'JavaScript (JS) is a lightweight interpreted or JIT-compiled programming language with first-class functions.',
      pictureId: 'skill89',
      rank: 1,
    },
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UserComponent],
      providers: [{ provide: AppService, useValue: appServiceSpy }],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(UserComponent);
    component = fixture.componentInstance;
  });

  beforeEach(() => {
    appServiceSpy.getUserInfo.and.returnValue(of(userInfoData));
    appServiceSpy.getBadges.and.returnValue(of(badgesData));
    appServiceSpy.getSkills.and.returnValue(of(skillsData));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('display userinfo', () => {
    it('should set userinfo on init', () => {
      fixture.detectChanges();

      expect(component.userInfo).toEqual(userInfoData);
      expect(appServiceSpy.getUserInfo).toHaveBeenCalled();
    });

    it('should set userInfoError on error', () => {
      appServiceSpy.getUserInfo.and.returnValue(throwError({ status: 500 }));

      fixture.detectChanges();

      expect(component.httpError.userInfoError).toBeDefined();
      expect(component.userInfo).toBeUndefined();
      expect(appServiceSpy.getUserInfo).toHaveBeenCalled();
    });
  });

  describe('display badges', () => {
    it('should set badges on init', () => {
      fixture.detectChanges();

      expect(component.badges).toEqual(badgesData);
      expect(appServiceSpy.getBadges).toHaveBeenCalled();
    });

    it('should set badgesError on error', () => {
      appServiceSpy.getBadges.and.returnValue(throwError({ status: 500 }));

      fixture.detectChanges();

      expect(component.httpError.badgesError).toBeDefined();
      expect(component.badges).toBeUndefined();
      expect(appServiceSpy.getBadges).toHaveBeenCalled();
    });
  });

  describe('display skills', () => {
    it('should set skills on init', () => {
      fixture.detectChanges();

      expect(component.skills).toEqual(skillsData);
      expect(appServiceSpy.getSkills).toHaveBeenCalled();
    });
  });

  it('should set skillsError on error', () => {
    appServiceSpy.getSkills.and.returnValue(throwError({ status: 500 }));

    fixture.detectChanges();

    expect(component.httpError.skillsError).toBeDefined();
    expect(component.skills).toBeUndefined();
    expect(appServiceSpy.getSkills).toHaveBeenCalled();
  });

  describe('display error', () => {
    it('should not set errors on init', () => {
      fixture.detectChanges();

      expect(component.httpError.userInfoError).toBeUndefined();
      expect(component.httpError.badgesError).toBeUndefined();
      expect(component.httpError.skillsError).toBeUndefined();
    });
  });

  describe('sort skills', () => {
    const skills = [
      {
        skillId: 123,
        description: "some skill",
        name: "skill",
        pictureId: "123",
        rank: 2,
      },
      {
        skillId: 123,
        description: "some skill",
        name: "skill",
        pictureId: "123",
        rank: 4,
      },
      {
        skillId: 123,
        description: "some skill",
        name: "skill",
        pictureId: "123",
        rank: 3,
      },
      {
        skillId: 123,
        description: "some skill",
        name: "skill",
        pictureId: "123",
        rank: 1,
      },
    ]

    it('should sort the skills by rank', () => {
      let skillsArray = skills;

      skillsArray = component.sortSkills(skillsArray);

      expect([skillsArray[0].rank, skillsArray[1].rank, skillsArray[2].rank])
        .toEqual([1,2,3]);
    });

    it('should shrink the skills array to 3 values', () => {
      let skillsArray = skills;

      skillsArray = component.sortSkills(skillsArray);

      expect(skillsArray.length).toEqual(3);
    });
  });
});
