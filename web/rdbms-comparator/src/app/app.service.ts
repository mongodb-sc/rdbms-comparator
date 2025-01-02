import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {BehaviorSubject, Observable, of} from 'rxjs';
import {Customer} from "./models/customer";
import {Response} from "./models/response";
import {Order} from "./models/Order";
import {Page} from "./models/page";
import {environment} from "../environments/environment";
import {Query} from "./models/Query";
import {FormGroup} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class AppService {

  useMongo : boolean = false;
  useMongoObservable: BehaviorSubject<boolean> = new BehaviorSubject(false);

  baseUrl:string = environment.baseUrl;


  constructor(private http: HttpClient) {
  }


  toggleDB(useMongo: boolean){
    this.useMongo = useMongo;
    this.useMongoObservable.next(this.useMongo);
  }


  getAllCustomers(formValues?: any, newPage?:number) :Observable<Page<Customer[]>> {
    let params = new HttpParams().set('db', this.useMongo ? 'mongodb' : 'pg').set('page', newPage ? newPage : 0)
    if (formValues.searchTerm) {
      params = params.set('searchTerm', formValues.searchTerm);
      return this.http.get<Page<Customer[]>>(`${this.baseUrl}/api/customers/search`, {params:params});
    } else {
      return this.http.post<Page<Customer[]>>(`${this.baseUrl}/api/customers/search?`,formValues, {params:params});
    }
    
  }

  getAllOrders(formValues?:any, newPage?: number) :Observable<Page<Order[]>> {
    return this.http.post<Page<Order[]>>(`${this.baseUrl}/api/orders/search?db=${this.useMongo ? 'mongodb' : 'pg'}&page=${newPage ? newPage : 0}`, formValues);
  }

  getQueries(threadName:string, threadId:number, millis:number) :Observable<Query[]> {
    let params = new HttpParams().set('threadName', threadName).set('threadId', threadId).set('time', millis)
    return this.http.get<Query[]>(`${this.baseUrl}/api/queries`, {params:params});
  }

  createCustomer(formValues?: any):Observable<Response<Customer>> {
    return this.http.post<Response<Customer>>(`${this.baseUrl}/api/customers`, formValues);
  }





}
