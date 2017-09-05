import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";

@Injectable()
export class SmokerserviceService {

  constructor(private http: HttpClient) { }


  public getText():Observable<Object> {
    return this.http.get('api/getstatus');

  }
}
