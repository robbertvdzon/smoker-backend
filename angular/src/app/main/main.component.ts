import { Component, OnInit } from '@angular/core';
import {SmokerserviceService} from "../smokerservice.service";
import {hasModifier} from "tslint";
import {Router} from "@angular/router";

export class Sample{
  date:String;
  temp:String;
}

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent implements OnInit {
  constructor(private router: Router, private smokerserviceService:SmokerserviceService ) { }
  id:String;
  currentTemp:String = "?";
  lastUpdate:String = "?";
  requiredTemp:String = "?";
  test:String;
  version:String;
  profiel:String;
  username:String;
  loggedIn:Boolean;
  chartAllesGeladen:Boolean = false;
  chart2UurGeladen:Boolean = false;
  last5samples:Sample[];

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
  chartDataAlles =  {
    chartType: 'LineChart',
    dataTable: [],
    options:
      {
        title: 'Gehele grafiek',
        curveType: 'function',
        hAxis: {title: 'Tijd',
          slantedText:true, slantedTextAngle:80},
        vAxis: {minValue: 80},
        legend: 'none'
      }
  };
  smokerData:any;
  dataTable: any[] = [];
  dataTableAlles: any[] = [];

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
      this.last5samples = [];

      let lastsamples = data["lastSamples"];
      for (var i=0; i<lastsamples.length; i++){
        let sample = new Sample();
        let date = new Date(lastsamples[i].date);
        var datestring = date.getDate()  + "-" + (date.getMonth()+1) + "-" + date.getFullYear() + " " +
           date.getHours() + ":" + date.getMinutes();
        sample.date = datestring;
        sample.temp = lastsamples[i].temp;
        this.last5samples.push(sample);
      }

      let date = new Date(data['lastUpdate']);
      var datestring = date.getDate()  + "-" + (date.getMonth()+1) + "-" + date.getFullYear() + " " +
        date.getHours() + ":" + date.getMinutes();
      this.lastUpdate = datestring;


      this.loadGrafiek("2uur");
      this.loadGrafiekAlles();
    });
    setInterval(()=>{this.reload()},4000);
  }

  private reload(){
    this.loadGrafiek("2uur");
    this.loadGrafiekAlles();
    this.loadState();
  }


  private loadGrafiek(range:String) {
    this.smokerserviceService.getAll(this.id, range).subscribe(data => {
      let smokerData:any[] = <Array<any>>data;

      this.dataTable = [];
      if (this.dataTable.length==0) {
        this.dataTable.push(['Tijd', 'Temp']);
      }
      for (var i=0; i<smokerData.length; i++){
        this.dataTable.push([new Date(smokerData[i].date), smokerData[i].temp]);
      }

      this.chart2UurGeladen = smokerData.length!=0;

      this.chartData2Uur =  {
        chartType: 'LineChart',
        dataTable: this.dataTable,
        options:
          {
            title: 'Laatste 2 uur',
            curveType: 'function',
            hAxis: {title: 'Tijd',
              slantedText:false, slantedTextAngle:0},
            vAxis: {minValue: 80},
            legend: 'none'
          }
      };
    });
  }

  private loadGrafiekAlles() {
    this.smokerserviceService.getAll(this.id, "alles").subscribe(data => {
      let smokerData:any[] = <Array<any>>data;

      this.dataTableAlles = [];
      if (this.dataTableAlles.length==0) {
        this.dataTableAlles.push(['Tijd', 'Temp']);
      }
      for (var i=0; i<smokerData.length; i++){
        this.dataTableAlles.push([new Date(smokerData[i].date), smokerData[i].temp]);
      }

      this.chartAllesGeladen  = smokerData.length!=0;

        this.chartDataAlles =  {
        chartType: 'LineChart',
        dataTable: this.dataTableAlles,
        options:
          {
            title: 'Gehele grafiek',
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
      this.last5samples = [];

      let lastsamples = data["lastSamples"];
      for (var i=0; i<lastsamples.length; i++){
        let sample = new Sample();
        let date = new Date(lastsamples[i].date);
        var datestring = date.getDate()  + "-" + (date.getMonth()+1) + "-" + date.getFullYear() + " " +
          date.getHours() + ":" + date.getMinutes();
        sample.date = datestring;
        sample.temp = lastsamples[i].temp;
        this.last5samples.push(sample);
      }

      let date = new Date(data['lastUpdate']);
      var datestring = date.getDate()  + "-" + (date.getMonth()+1) + "-" + date.getFullYear() + " " +
        date.getHours() + ":" + date.getMinutes();
      this.lastUpdate = datestring;
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
