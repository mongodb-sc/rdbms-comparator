import {Component, OnInit} from '@angular/core';
import {AppService} from "../app.service";
import {Observable} from "rxjs";
import {Customer} from "../models/customer";
import {reportUnhandledError} from "rxjs/internal/util/reportUnhandledError";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-customer-search',
  templateUrl: './customer-search.component.html',
  styleUrl: './customer-search.component.sass'
})
export class CustomerSearchComponent implements OnInit{

  data?: Observable<Customer[]>
  elapsed: number = 0;
  isCollapsed = false;

  searchForm: FormGroup;

  constructor(private service: AppService, private fb:FormBuilder) {

    this.searchForm = this.fb.group({
      firstName: [''],
      lastName: [''],
      title: [''],
      city: [''],
      state:[''],
      zip:[''],
      street:[''],
      phones: this.fb.group({
        type: [''],
        number:[''],
      }),
      emails: this.fb.group({
        type: [''],
        email: ['']
      })
    })


  }

  ngOnInit(): void {
    performance.mark(`GetAllCustomers-${this.service.useMongo}-started`)
    this.data = this.service.getAllCustomers()
    performance.mark(`GetAllCustomers-${this.service.useMongo}-finished`)
    const result = performance.measure(`GetAllCustomers-${this.service.useMongo}-duration`,`GetAllCustomers-${this.service.useMongo}-started`,`GetAllCustomers-${this.service.useMongo}-finished` )
    this.elapsed = result.duration;
  }

  refresh(){
    performance.mark(`GetAllCustomers-${this.service.useMongo}-started`)
    this.data = this.service.getAllCustomers(this.searchForm.value)
    performance.mark(`GetAllCustomers-${this.service.useMongo}-finished`)
    const result = performance.measure(`GetAllCustomers-${this.service.useMongo}-duration`,`GetAllCustomers-${this.service.useMongo}-started`,`GetAllCustomers-${this.service.useMongo}-finished` )
    this.elapsed = result.duration;
  }



}
