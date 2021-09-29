import { Component } from '@angular/core';
import { UserInfoDto } from 'src/model/UserInfoDto';
import { TranslateService } from '@ngx-translate/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  userInfo!: UserInfoDto;

  constructor(private translate: TranslateService) {
    translate.setDefaultLang('en');
  }

  onUserFound(userInfo: UserInfoDto) {
    this.userInfo = userInfo;
  }

  useLanguage(language: string): void {
    this.translate.use(language);
  }

  isCurrentLanguage(language: string): boolean {
    return this.translate.currentLang === language;
  }
}
