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

  setAuth(uploadAuthKey: String) {
    const req = this.http.get(environment.putAuthKey+"?key="+uploadAuthKey)
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
