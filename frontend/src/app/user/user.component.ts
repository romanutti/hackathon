import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { BadgeDto } from 'src/model/BadgeDto';
import { SkillDto } from 'src/model/SkillDto';
import { UserError } from 'src/model/user/UserError';
import { UserInfoDto } from 'src/model/UserInfoDto';
import { AppService } from '../app.service';
import { RatingComponent } from './rating/rating.component';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.scss'],
})
export class UserComponent implements OnChanges {
  @Input()
  userInfo: UserInfoDto | undefined;
  badges: BadgeDto[] | undefined;
  skills: SkillDto[] | undefined;
  httpError: UserError = {};

  constructor(
    private readonly appService: AppService,
    private modalService: NgbModal
  ) {}

  ngOnChanges(changes: SimpleChanges) {
    if (this.userInfo) {
      this.userInfo = changes.userInfo.currentValue;
      this.loadBadges();
      this.loadSkills();
    }
  }

  loadUserInfo() {
    if (this.userInfo) {
      this.appService.getUserInfo(this.userInfo.id).subscribe({
        next: (userInfo) => {
          this.userInfo = userInfo;
          this.httpError.userInfoError = undefined;
        },
        error: (error: HttpErrorResponse) => {
          this.httpError.userInfoError = error.status;
        },
      });
    }
  }

  loadBadges() {
    this.badges = undefined;
    if (this.userInfo) {
      this.appService.getBadges(this.userInfo.id).subscribe({
        next: (badges) => {
          this.badges = badges;
          this.httpError.badgesError = undefined;
        },
        error: (error: HttpErrorResponse) => {
          this.httpError.badgesError = error.status;
        },
      });
    }
  }

  sortSkills(skills: SkillDto[]): SkillDto[] {
    let sortedSkills: SkillDto[] = [
      ...skills.sort((a, b) => b.points - a.points),
    ];
    if (skills.length > 3) {
      sortedSkills = sortedSkills.splice(0, 3);
    }

    sortedSkills = sortedSkills.map((skill, index) => {
      skill.rank = index + 1;
      return skill;
    });

    const firstValue = sortedSkills[0];
    sortedSkills[0] = sortedSkills[1];
    sortedSkills[1] = firstValue;

    return sortedSkills;
  }

  loadSkills() {
    this.skills = undefined;
    if (this.userInfo) {
      this.appService.getSkills(this.userInfo.id).subscribe({
        next: (skills) => {
          const sortedSkills: SkillDto[] = this.sortSkills(skills);
          this.skills = sortedSkills;
          this.httpError.skillsError = undefined;
        },
        error: (error: HttpErrorResponse) => {
          this.httpError.skillsError = error.status;
        },
      });
    }
  }

  open() {
    const modalRef = this.modalService.open(RatingComponent);
    modalRef.componentInstance.userInfo = this.userInfo;
  }
}
