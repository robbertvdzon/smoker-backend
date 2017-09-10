import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {SmokerserviceService} from "../smokerservice.service";

@Component({
  selector: 'app-grafiek',
  templateUrl: './grafiek.component.html',
  styleUrls: ['./grafiek.component.scss']
})
export class GrafiekComponent implements OnInit {
  id: number;
  pieChartData =  {
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
      this.id = +params['id']; // (+) converts string 'id' to a number
      this.loadGrafiek();
    });
  }

  private loadGrafiek() {
    this.smokerserviceService.getAll().subscribe(data => {
      let smokerData:any[] = <Array<any>>data;
      this.pieChartData.dataTable=[];
      this.pieChartData.dataTable.push(['Tijd', 'Temp', 'Fan'])
      for (var i=0; i<smokerData.length; i++){
        this.pieChartData.dataTable.push([new Date(smokerData[i].date), smokerData[i].temp, smokerData[i].sturing]);
      }
    });
  }
}
