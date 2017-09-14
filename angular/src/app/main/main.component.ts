import { Component, OnInit } from '@angular/core';
import {SmokerserviceService} from "../smokerservice.service";
import {hasModifier} from "tslint";
import {Router} from "@angular/router";

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {
  constructor(private router: Router, private smokerserviceService:SmokerserviceService ) { }
  id:String;
  test:String;
  version:String;
  profiel:String;
  username:String;
  loggedIn:Boolean;

  chartData =  {
    chartType: 'LineChart',
    dataTable: [],
    options:
      {
        title: 'Laatste 2 uur',
        curveType: 'function',
        hAxis: {title: 'Tijd',
          slantedText:true, slantedTextAngle:80},
        vAxis: {minValue: 80},
        legend: 'none'
      }
  };
  smokerData:any;
  dataTable: any[] = [];

  ngOnInit() {
    this.smokerserviceService.getStatus().subscribe(data => {
      // Read the result field from the JSON response.
      this.test = ""+ data['version'];
      this.username = ""+ data['username'];
      this.version = ""+ data['version'];
      this.profiel = ""+ data['profiel'];
      this.id= "" + data['userid'];
      this.loggedIn = this.username!="null";

      this.loadGrafiek("2uur");
      // this.test = "bla";
    });
  }


  private loadGrafiek(range:String) {
    this.smokerserviceService.getAll(this.id, range).subscribe(data => {
      let smokerData:any[] = <Array<any>>data;

      this.dataTable = [];
      if (this.dataTable.length==0) {
        this.dataTable.push(['Tijd', 'Temp']);
      }
      for (var i=0; i<smokerData.length; i++){
        this.dataTable.push([this.dataTable.length, smokerData[i].temp]);
      }

      this.chartData =  {
        chartType: 'LineChart',
        dataTable: this.dataTable,
        options:
          {
            title: 'Laatste 2 uur',
            curveType: 'function',
            hAxis: {title: 'Tijd',
              slantedText:false, slantedTextAngle:80},
            vAxis: {minValue: 80},
            legend: 'none'
          }
      };


    });
  }

  grafiek():void{
    this.router.navigate(['grafiek',this.id]);
  }

  settings():void{
    this.router.navigate(['settings']);
  }
}
