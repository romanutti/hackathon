import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { UserInfoDto } from 'src/model/UserInfoDto';
import { BadgeDto } from 'src/model/BadgeDto';
import { SkillDto } from 'src/model/SkillDto';
import { Levels } from 'src/model/rating/Levels';

@Injectable({
  providedIn: 'root',
})
export class AppService {
  private readonly API_USER_BASE_PATH = 'api/user';
  private readonly API_USER_SEARCH_PATH = '/search';
  private readonly API_USER_BADGES_PATH = '/badges';
  private readonly API_USER_SKILLS_PATH = '/skills';
  private readonly API_USER_RATE_PATH = '/rating';

  constructor(private http: HttpClient) {}

  getUserInfo(term: string): Observable<UserInfoDto> {
    return this.http.get<UserInfoDto>(
      this.API_USER_BASE_PATH + this.API_USER_SEARCH_PATH + '?term=' + term
    );
  }

  getBadges(userId: string): Observable<BadgeDto[]> {
    return this.http.get<BadgeDto[]>(
      this.API_USER_BASE_PATH + '/' + userId + this.API_USER_BADGES_PATH
    );
  }

  getSkills(userId: string): Observable<SkillDto[]> {
    return this.http.get<SkillDto[]>(
      this.API_USER_BASE_PATH + '/' + userId + this.API_USER_SKILLS_PATH
    );
  }

  rateSkills(userId: string, skillId: number, level: Levels): Observable<null> {
    return this.http.post<null>(
      this.API_USER_BASE_PATH +
        '/' +
        userId +
        this.API_USER_RATE_PATH +
        '/' +
        skillId,
      { level: level }
    );
  }
}
