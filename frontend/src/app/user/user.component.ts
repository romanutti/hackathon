import { HttpErrorResponse } from '@angular/common/http';
import {Component, Input, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import { BadgeDto } from 'src/model/BadgeDto';
import { SkillDto } from 'src/model/SkillDto';
import { UserError } from 'src/model/user/UserError';
import { UserInfoDto } from 'src/model/UserInfoDto';
import { AppService } from '../app.service';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent implements OnInit, OnChanges {
  @Input()
  userId: string = "";

  mockSkills = [
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
      name: "skillqwertzuiojhgfdsdfghjlk",
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

  mockBadges: BadgeDto[] = [
    {id: "1", description: "Hackathon"},
    {id: "2", description: "Java King"},
    {id: "3", description: "5 Votes"},
  ];

  userInfo: UserInfoDto | undefined;
  badges: BadgeDto[] | undefined = this.mockBadges;
  skills: SkillDto[] | undefined = this.sortSkills(this.mockSkills);
  httpError: UserError = {};

  constructor(private readonly appService: AppService) {}

  ngOnInit(): void {
    this.loadUserInfo();
    this.loadBadges();
    //this.loadSkills();
  }

  ngOnChanges(changes: SimpleChanges) {
    const newUserId: string = changes.userId.currentValue;
    if(newUserId) {
      this.userId = newUserId;
      this.loadUserInfo()
    }
  }

  loadUserInfo() {
    this.appService.getUserInfo(this.userId).subscribe({
      next: (userInfo) => {
        this.userInfo = userInfo;
      },
      error: (error: HttpErrorResponse) => {
        this.httpError.userInfoError = error.status;
      },
    });
  }

  loadBadges() {
    this.appService.getBadges(this.userId).subscribe({
      next: (badges) => {
        this.badges = badges;
      },
      error: (error: HttpErrorResponse) => {
        this.httpError.badgesError = error.status;
      },
    });
  }

  sortSkills(skills: SkillDto[]): SkillDto[] {
    let sortedSkills = skills.sort((a, b) => a.rank - b.rank);
    if (skills.length > 3) {
      sortedSkills = sortedSkills.splice(0, 3);
    }

    const firstValue = sortedSkills[0];
    sortedSkills[0] = sortedSkills[1];
    sortedSkills[1] = firstValue;

    return sortedSkills;
  }

  loadSkills() {
    this.appService.getSkills(this.userId).subscribe({
      next: (skills) => {
        const sortedSkills: SkillDto[] = this.sortSkills(skills);
        console.log(sortedSkills)
        this.skills = sortedSkills;
      },
      error: (error: HttpErrorResponse) => {
        this.httpError.skillsError = error.status;
      },
    });
  }
}
