import { Component, OnInit } from '@angular/core';
import {Router} from "@angular/router";
import {SmokerserviceService} from "./smokerservice.service";
import { User } from './shared/model/user.model';
import { KeycloakService } from './shared/service/keycloak.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  title = 'app';
  id:String;
  version:String;
  profiel:String;
  username:String;
  profile: User;

  constructor(private router: Router, private smokerserviceService:SmokerserviceService, private keycloakService: KeycloakService) {
  }

  grafiek():void{
    this.router.navigate(['grafiek',this.id]);
  }

  main():void{
    this.router.navigate(['main']);
  }

  settings():void{
    this.router.navigate(['settings']);
  }


  ngOnInit() {
    this.profile = this.keycloakService.getUser();
    this.smokerserviceService.getStatus().subscribe(data => {
      // Read the result field from the JSON response.
      this.id= "" + data['userid'];
      this.username = ""+ data['username'];
      this.version = ""+ data['version'];
      this.profiel = ""+ data['profiel'];
      // this.test = "bla";
    });
  }

  connectToFacebook() {
    this.router.navigate(['settings']);

  }
}
