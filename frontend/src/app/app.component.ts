import { Component } from '@angular/core';
import { UserInfoDto } from 'src/model/UserInfoDto';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'frontend';
  userInfo!: UserInfoDto;

  onUserFound(userInfo: UserInfoDto) {
    this.userInfo = userInfo;
  }
}
