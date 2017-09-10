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
      this.userId = params['id'];
      this.loadGrafiek("2 uur");
    });
  }

  private loadGrafiek(range:String) {
    this.smokerserviceService.getAll(this.userId, range).subscribe(data => {
      let smokerData:any[] = <Array<any>>data;
      this.pieChartData.dataTable=[];
      this.pieChartData.dataTable.push(['Tijd', 'Temp', 'Fan'])
      for (var i=0; i<smokerData.length; i++){
        this.pieChartData.dataTable.push([new Date(smokerData[i].date), smokerData[i].temp, smokerData[i].sturing]);
      }
    });
  }

  setPlan(value) {
    //if you're on older versions of ES, use for-in instead
    this.loadGrafiek(value);
  }
}
