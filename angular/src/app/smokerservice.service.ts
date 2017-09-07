import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import { environment } from '../environments/environment';

@Injectable()
export class SmokerserviceService {

  constructor(private http: HttpClient) { }


  public getText():Observable<Object> {
    return this.http.get(environment.getStatusUrl);

  }
}
