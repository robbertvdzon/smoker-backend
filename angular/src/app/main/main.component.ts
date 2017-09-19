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
  currentTemp:String = "?";
  requiredTemp:String = "?";
  test:String;
  version:String;
  profiel:String;
  username:String;
  loggedIn:Boolean;

  chartData2Uur =  {
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
  chartData8Uur =  {
    chartType: 'LineChart',
    dataTable: [],
    options:
      {
        title: 'Laatste 8 uur',
        curveType: 'function',
        hAxis: {title: 'Tijd',
          slantedText:true, slantedTextAngle:80},
        vAxis: {minValue: 80},
        legend: 'none'
      }
  };
  smokerData:any;
  dataTable: any[] = [];
  dataTable8uur: any[] = [];

  ngOnInit() {
    // Deze call zou helemaal niet nodig moeten zijn! We hebben het id al!
    this.smokerserviceService.getStatus().subscribe(data => {
      // Read the result field from the JSON response.
      this.test = ""+ data['version'];
      this.username = ""+ data['username'];
      this.version = ""+ data['version'];
      this.profiel = ""+ data['profiel'];
      this.id= "" + data['userid'];
      this.loggedIn = this.username!="null";
      this.currentTemp = ""+ data['lastTemp']; // deze laad ik ook al in de loadState call!
      this.requiredTemp = ""+ data['requiredTemp'];

      this.loadGrafiek("2uur");
      this.loadGrafiek8Uur();
    });
    setInterval(()=>{this.loadState();},4000);
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

      this.chartData2Uur =  {
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

  private loadGrafiek8Uur() {
    this.smokerserviceService.getAll(this.id, "8uur").subscribe(data => {
      let smokerData:any[] = <Array<any>>data;

      this.dataTable8uur = [];
      if (this.dataTable8uur.length==0) {
        this.dataTable8uur.push(['Tijd', 'Temp']);
      }
      for (var i=0; i<smokerData.length; i++){
        this.dataTable8uur.push([this.dataTable8uur.length, smokerData[i].temp]);
      }

      this.chartData8Uur =  {
        chartType: 'LineChart',
        dataTable: this.dataTable8uur,
        options:
          {
            title: 'Laatste 8 uur',
            curveType: 'function',
            hAxis: {title: 'Tijd',
              slantedText:false, slantedTextAngle:80},
            vAxis: {minValue: 80},
            legend: 'none'
          }
      };
    });
  }

  private loadState() {
    this.smokerserviceService.getStatus().subscribe(data => {
      this.currentTemp = ""+ data['lastTemp'];
      this.requiredTemp = ""+ data['requiredTemp'];
    });
  }


  grafiek():void{
    this.router.navigate(['grafiek',this.id]);
  }

  settings():void{
    this.router.navigate(['settings']);
  }

  addRequiredTemp(add:number){
    this.smokerserviceService.addRequiredTemp(add).subscribe(data => {
      this.requiredTemp = ""+ data['requiredTemp'];

    });
  }

  min5():void{
    this.addRequiredTemp(-5);
  }
  min10():void{
    this.addRequiredTemp(-5);
  }
  plus10():void{
    this.addRequiredTemp(10);
  }
  plus5():void{
    this.addRequiredTemp(5);
  }

}
