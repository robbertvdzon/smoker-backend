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
  smokerData:any;
  pieChartData =  {
    chartType: 'PieChart',
    dataTable: [
      ['Task', 'Hours per Day'],
      ['Work',     11],
      ['Eat',      2],
      ['Commute',  2],
      ['Watch TV', 2],
      ['Sleep',    7]
    ],
    options: {'title': 'Tasks'},
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
      this.smokerData= data;
    });
  }
}
