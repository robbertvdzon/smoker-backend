import { Component, OnInit } from '@angular/core';
import {SmokerserviceService} from "../smokerservice.service";
import {hasModifier} from "tslint";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {
  constructor(private smokerserviceService:SmokerserviceService ) { }
  test:String;
  version:String;
  profiel:String;
  username:String;

  ngOnInit() {
    this.smokerserviceService.getText().subscribe(data => {
      // Read the result field from the JSON response.
      this.test = ""+ data['version'];
      this.username = ""+ data['username'];
      this.version = ""+ data['version'];
      this.profiel = ""+ data['profiel'];
      // this.test = "bla";
    });
  }

}
