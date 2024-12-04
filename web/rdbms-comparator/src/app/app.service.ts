import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import { Observable } from 'rxjs';
import {Customer} from "./models/customer";
import {Order} from "./models/Order";
import {Page} from "./models/page";
import { environment } from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  useMongo : boolean = false;
  baseUrl:string = environment.baseUrl;

  constructor(private http: HttpClient) { }


  toggleDB(useMongo: boolean){
    this.useMongo = useMongo;
  }


  getAllCustomers(formValues?: any, newPage?:number) :Observable<Page<Customer[]>> {
    return this.http.post<Page<Customer[]>>(`${this.baseUrl}/api/customers/search?db=${this.useMongo ? 'mongodb' : 'pg'}&page=${newPage ? newPage : 0}`, formValues);
  }

  getAllOrders(formValues?:any, newPage?: number) :Observable<Page<Order[]>> {
    return this.http.post<Page<Order[]>>(`${this.baseUrl}/api/orders/search?db=${this.useMongo ? 'mongodb' : 'pg'}&page=${newPage ? newPage : 0}`, formValues);
  }





}
