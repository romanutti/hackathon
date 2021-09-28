import {
  Component,
  EventEmitter,
  Output,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { Subscription } from 'rxjs';
import { UserInfoDto } from '../../model/UserInfoDto';
import { HttpErrorResponse } from '@angular/common/http';
import { AppService } from '../app.service';
import { FormControl, FormGroup } from '@angular/forms';
import { debounceTime } from 'rxjs/operators';
import { SearchError } from '../../model/search/SearchError';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss'],
})
export class SearchComponent implements OnInit, OnDestroy {
  onInputChange$: Subscription | undefined;
  httpError: SearchError = {};
  debounceMilliseconds: number = 300;

  search: FormGroup = new FormGroup({
    searchTerm: new FormControl(),
  });

  @Output()
  foundUser = new EventEmitter<UserInfoDto>();

  constructor(private readonly appService: AppService) {}

  ngOnInit() {
    this.onInputChange$ = this.search.valueChanges
      .pipe(debounceTime(this.debounceMilliseconds))
      .subscribe((data) => this.searchForTerm(data.searchTerm));
  }

  ngOnDestroy() {
    if (this.onInputChange$) {
      this.onInputChange$.unsubscribe();
    }
  }

  searchForTerm(searchTerm: string): void {
    this.appService.getUserInfo(searchTerm).subscribe({
      next: (userInfo: UserInfoDto) => {
        this.foundUser.emit(userInfo);
      },
      error: (error: HttpErrorResponse) => {
        this.httpError.searchError = error.status;
      },
    });
  }
}
