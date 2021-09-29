import { HttpErrorResponse } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, OperatorFunction } from 'rxjs';
import { debounceTime, distinctUntilChanged, map } from 'rxjs/operators';
import { Levels } from 'src/model/rating/Levels';
import { RatingError } from 'src/model/rating/RatingError';
import { SkillDto } from 'src/model/SkillDto';
import { UserInfoDto } from 'src/model/UserInfoDto';
import { AppService } from '../../app.service';

@Component({
  selector: 'app-rating',
  templateUrl: './rating.component.html',
  styleUrls: ['./rating.component.scss'],
})
export class RatingComponent implements OnInit {
  skills: SkillDto[] = [];
  skill: SkillDto | undefined;
  level: Levels | undefined;

  @Input()
  userInfo: UserInfoDto = {
    id: '',
    firstName: '',
    lastName: '',
    jobTitle: '',
    pictureId: '',
  };

  httpError: RatingError = {};

  constructor(private readonly appService: AppService) {}
  ngOnInit(): void {
    this.appService
      .getSkills(this.userInfo.id)
      .subscribe((skills) => (this.skills = skills));
  }

  search: OperatorFunction<string, readonly SkillDto[]> = (
    text$: Observable<string>
  ) =>
    text$.pipe(
      debounceTime(200),
      distinctUntilChanged(),
      map((term) =>
        term.length < 2
          ? []
          : this.skills
              .filter(
                (skill) =>
                  skill.name.toLowerCase().indexOf(term.toLowerCase()) > -1
              )
              .slice(0, 10)
      )
    );

  formatter = (x: { name: string }) => x.name;

  rateSkills() {
    if (this.skill && this.level) {
      this.appService
        .rateSkills(this.userInfo.id, this.skill.skillId, this.level)
        .subscribe({
          error: (error: HttpErrorResponse) => {
            this.httpError.ratingError = error.status;
          },
        });
    }
  }
}
