import {Injectable, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs/Observable";
import { environment } from '../environments/environment';

@Injectable()
export class SmokerserviceService {

  constructor(private http: HttpClient) { }


  public getStatus():Observable<Object> {
    return this.http.get(environment.getStatusUrl);

  }

  public getAll(userid:String, range:String):Observable<Object> {
    return this.http.get(environment.getAllUrl+"?userid="+userid+"&range="+range);
  }

  setSettings(uploadAuthKey: String, openbaar: Boolean) {
    const req = this.http.get(environment.putSettings+"?key="+uploadAuthKey+"&openbaar="+openbaar)
      .subscribe(
        res => {
          console.log(res);
        },
        err => {
          console.log("Error occured");
        }
      );
  }

  clearData() {
    const req = this.http.get(environment.clearDataUrl)
      .subscribe(
        res => {
          console.log(res);
        },
        err => {
          console.log("Error occured");
        }
      );
  }

  addRequiredTemp(add: number): Observable<Object> {
    return this.http.get(environment.getAddRequiredTempUrl+"?add="+add);
  }
}
