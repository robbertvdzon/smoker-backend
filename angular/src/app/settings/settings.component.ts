import {Component, Input, OnInit} from '@angular/core';
import {SmokerserviceService} from "../smokerservice.service";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  constructor(private smokerserviceService:SmokerserviceService ) { }
  test:String;
  version:String;
  profiel
  username:String;
  @Input() uploadAuthKey: String;


  ngOnInit() {
    this.smokerserviceService.getStatus().subscribe(data => {
      // Read the result field from the JSON response.
      this.test = "" + data['version'];
      this.username = "" + data['username'];
      this.version = "" + data['version'];
      this.profiel = "" + data['profiel'];
      this.uploadAuthKey= "" + data['user']['uploadAuthKey'];

      // this.test = "bla";
    });
  }

  setAuth():void{
    this.smokerserviceService.setAuth(this.uploadAuthKey);
  }

}
