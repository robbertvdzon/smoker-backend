import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {SmokerserviceService} from "../smokerservice.service";

@Component({
  selector: 'app-grafiek',
  templateUrl: './grafiek.component.html',
  styleUrls: ['./grafiek.component.scss']
})
export class GrafiekComponent implements OnInit {
  userId: String;
  smokerData:any;
  dataTable: any[] = [];

  chartData =  {
    chartType: 'LineChart',
    dataTable: [],
    options:
      {
        title: 'Smoker',
        curveType: 'function',
        hAxis: {title: 'Tijd',
          slantedText:true, slantedTextAngle:80},
        vAxis: {minValue: 80},
      }
  };

  constructor(private route: ActivatedRoute, private smokerserviceService:SmokerserviceService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.userId = params['id']; // (+) converts string 'id' to a number
      this.loadGrafiek("2uur");
    });
  }

  setPlan(value) {
    this.loadGrafiek(value);
  }

  private loadGrafiek(range:String) {
    this.smokerserviceService.getAll(this.userId, range).subscribe(data => {
      let smokerData:any[] = <Array<any>>data;

      this.dataTable = [];
      if (this.dataTable.length==0) {
        this.dataTable.push(['Tijd', 'Temp', 'Fan']);
      }
      for (var i=0; i<smokerData.length; i++){
        this.dataTable.push([this.dataTable.length, smokerData[i].temp, smokerData[i].sturing]);
      }

      this.chartData =  {
        chartType: 'LineChart',
        dataTable: this.dataTable,
        options:
          {
            title: 'Smoker',
            curveType: 'function',
            hAxis: {title: 'Tijd',
              slantedText:true, slantedTextAngle:80},
            vAxis: {minValue: 80},
          }
      };


    });
  }
}
