import {Component, Input, OnInit} from '@angular/core';
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
  selectedRange="2uur";
  selectedType="temp";

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
      this.loadGrafiek();
    });
  }

  setPlan(value) {
    this.selectedRange=value;
    this.loadGrafiek();
  }

  setType(type) {
    this.selectedType=type;
    this.loadGrafiek();
  }

  private loadGrafiek() {
    let range = this.selectedRange;
    this.smokerserviceService.getAll(this.userId, range).subscribe(data => {
      let smokerData:any[] = <Array<any>>data;
      let type = this.selectedType;

      this.dataTable = [];
      let header = [];
      header.push('Tijd');
      if (type=="temp" || type=="beide"){
        header.push('Temp');
      }
      if (type=="sturing" || type=="beide"){
        header.push('Fan');
      }
      this.dataTable.push(header);
      for (var i=0; i<smokerData.length; i++){
        let value = [];
        value.push(this.dataTable.length);
        if (type=="temp" || type=="beide"){
          value.push(smokerData[i].temp);
        }
        if (type=="sturing" || type=="beide"){
          value.push(smokerData[i].sturing);
        }
        this.dataTable.push(value);
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
