import { HttpErrorResponse } from '@angular/common/http';
import {
  Component,
  Input,
  OnChanges,
  OnInit,
  SimpleChanges,
} from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
  userId: string = '';

  userInfo: UserInfoDto | undefined;
  badges: BadgeDto[] | undefined;
  skills: SkillDto[] | undefined;
  httpError: UserError = {};

  constructor(
    private readonly appService: AppService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadUserInfo();
    this.loadBadges();
    this.loadSkills();
  }

  ngOnChanges(changes: SimpleChanges) {
    this.userId = changes.userId.currentValue;
    this.loadUserInfo();
    this.loadBadges();
    this.loadSkills();
  }

  loadUserInfo() {
    this.appService.getUserInfo(this.userId).subscribe({
      next: (userInfo) => {
        this.userInfo = userInfo;
        this.httpError.userInfoError = undefined;
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
        this.httpError.badgesError = undefined;
      },
      error: (error: HttpErrorResponse) => {
        this.httpError.badgesError = error.status;
      },
    });
  }

  loadSkills() {
    this.appService.getSkills(this.userId).subscribe({
      next: (skills) => {
        this.skills = skills;
        this.httpError.skillsError = undefined;
      },
      error: (error: HttpErrorResponse) => {
        this.httpError.skillsError = error.status;
      },
    });
  }

  open(content: any) {
    this.modalService
      .open(content, { ariaLabelledBy: 'modal-basic-title' })
      .result.then(
        (result) => {},
        (reason) => {}
      );
  }
}
