import { Component } from '@angular/core';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent {

  userInput!: string;

  constructor() { }

  onInputChange(newInput: string) {
    this.userInput = newInput;
    console.log(this.userInput);
  }

}
