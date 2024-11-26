import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {AppService} from "./app.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.sass'
})
export class AppComponent {
  title = 'Mongo Logistics';
  useMongo:boolean  = false;


  constructor(private service:AppService) {
  }

  toggleDB() {
    this.useMongo = !this.useMongo;
    this.service.toggleDB(this.useMongo);
  }


}
