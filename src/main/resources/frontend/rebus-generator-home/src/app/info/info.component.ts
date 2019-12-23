import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-info',
  templateUrl: './info.component.html',
  styleUrls: ['./info.component.css']
})
export class InfoComponent implements OnInit {
  
  home: string;

  constructor() { }

  ngOnInit() {
    this.home = 'Back to rebus generator';
  }

}
