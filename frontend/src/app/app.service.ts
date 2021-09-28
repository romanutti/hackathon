import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { UserInfoDto } from 'src/model/UserInfoDto';
import { BadgeDto } from 'src/model/BadgeDto';
import { SkillDto } from 'src/model/SkillDto';

@Injectable({
  providedIn: 'root',
})
export class AppService {
  private readonly API_USER_BASE_PATH = 'api/user';
  private readonly API_USER_SEARCH_PATH = '/search';
  private readonly API_USER_BADGES_PATH = '/badges';
  private readonly API_USER_SKILLS_PATH = '/skills';

  constructor(private http: HttpClient) {}

  getUserInfo(term: string): Observable<UserInfoDto> {
    return this.http.get<UserInfoDto>(
      this.API_USER_BASE_PATH + this.API_USER_SEARCH_PATH + '?term=' + term
    );
  }

  getBadges(userId: string): Observable<BadgeDto[]> {
    const badgeData: BadgeDto[] = [
      {
        iconId:
          'https://res.cloudinary.com/practicaldev/image/fetch/s--oarf4XxB--/c_limit,f_auto,fl_progressive,q_80,w_375/https://dev-to-uploads.s3.amazonaws.com/uploads/badge/badge_image/16/js-badge.png',
        description: 'Zuehlke Hackathony',
      },
      {
        iconId:
          'https://res.cloudinary.com/practicaldev/image/fetch/s--X91eHXRc--/c_limit,f_auto,fl_progressive,q_80,w_375/https://dev-to-uploads.s3.amazonaws.com/uploads/badge/badge_image/17/57802960-3482e680-7725-11e9-9354-77712df6a949.png',
        description: 'Zuehlke Hackathony',
      },
      {
        iconId:
          'https://res.cloudinary.com/practicaldev/image/fetch/s--VHkumAEb--/c_limit,f_auto,fl_progressive,q_80,w_375/https://dev-to-uploads.s3.amazonaws.com/uploads/badge/badge_image/26/react-sticker.png',
        description: 'Zuehlke Hackathony',
      },
      {
        iconId:
          'https://res.cloudinary.com/practicaldev/image/fetch/s--gBcBoIco--/c_limit,f_auto,fl_progressive,q_80,w_375/https://dev-to-uploads.s3.amazonaws.com/uploads/badge/badge_image/29/graphql-sticker.png',
        description: 'Zuehlke Hackathony',
      },
      {
        iconId:
          'https://res.cloudinary.com/practicaldev/image/fetch/s--mLVsIr7I--/c_limit,f_auto,fl_progressive,q_80,w_375/https://dev-to-uploads.s3.amazonaws.com/uploads/badge/badge_image/89/next-js-badge.png',
        description: 'Zuehlke Hackathony',
      },
    ];
    return of(badgeData);
    //return this.http.get<BadgeDto[]>(
    //  this.API_USER_BASE_PATH + '/' + userId + this.API_USER_BADGES_PATH
    //);
  }

  getSkills(userId: string): Observable<SkillDto[]> {
    return this.http.get<SkillDto[]>(
      this.API_USER_BASE_PATH + '/' + userId + this.API_USER_SKILLS_PATH
    );
  }
}
