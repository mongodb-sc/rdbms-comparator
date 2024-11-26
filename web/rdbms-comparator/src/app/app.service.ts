import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import { Observable } from 'rxjs';
import {Customer} from "./models/customer";
import {Order} from "./models/Order";
import {Page} from "./models/page";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  useMongo : boolean = false;

  constructor(private http: HttpClient) { }


  toggleDB(useMongo: boolean){
    this.useMongo = useMongo;
  }


  getAllCustomers(formValues?: any) :Observable<Customer[]> {
    let params = new HttpParams();
    if (formValues) {
      for (let key in formValues){
        if  (formValues[key] === null || formValues[key] === ''){
          delete formValues[key];
        } else if ( typeof formValues[key] === 'object'){
          let subObj = formValues[key];
          for (let subkey in subObj){
            if (subObj[subkey] === null || subObj[subkey] === ''){
              delete formValues[key]
            } else {
              formValues[key] = JSON.stringify(subObj);
            }
          }
        }
      }
      console.log(formValues)
      params = params.appendAll(formValues);
    }
    if (this.useMongo) {
      console.log('Trying to set the DB param');
      params = params.append('db', 'mongodb');
    }
    return this.http.get<Customer[]>('http://localhost:8080/api/customers', {params: params});
  }

  getAllOrders(formValues?:any) :Observable<Page<Order[]>> {
    let params = new HttpParams();
    if (this.useMongo) {
      params = params.set('db', 'mongodb');
    }
    return this.http.post<Page<Order[]>>(`http://localhost:8080/api/orders/search?db=${this.useMongo ? 'mongodb' : 'pg'}`, formValues);
  }





}
