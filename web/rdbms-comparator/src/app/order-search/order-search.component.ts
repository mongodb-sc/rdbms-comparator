import {Component, OnInit} from '@angular/core';
import {AppService} from "../app.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {Observable} from "rxjs";
import {Customer} from "../models/customer";
import {Order} from "../models/Order";
import {Address} from "../models/address";
import {Page} from "../models/page";

@Component({
  selector: 'app-order-search',
  templateUrl: './order-search.component.html',
  styleUrl: './order-search.component.sass'
})
export class OrderSearchComponent implements OnInit{

  data?: Page<Order[]>
  elapsed = {
    duration:  0,
    elapsedLabel: '',
    loading:true
  }

  isCollapsed = false;
  rowCollapsed: Array<boolean> = new Array<boolean>(100);
  searchForm: FormGroup;
  useMongo:boolean;

  constructor(private service:AppService, private fb:FormBuilder) {
    this.rowCollapsed.fill(true);
    this.useMongo = this.service.useMongo;

    this.searchForm = this.fb.group({
      lucene:[false],
      id:[''],
      orderDate:[''],
      warehouseId:['',Validators.pattern('^[0-9]')],
      fillDate:[''],
      purchaseOrder:[''],
      invoiceId:['',Validators.pattern('^[0-9]')],
      invoiceDate:[''],
      deliveryMethod:[''],
      weight:['', Validators.pattern('^[0-9]')],
      totalPieces:[''],
      pickDate:[''],
      shippingMethod:[''],
      billingDept:['', Validators.pattern('^[0-9]')],
      orderStatus:[''],
      shippingStatus:[''],
      deliveryDate:[''],
      orderType: ['', Validators.pattern('^[0-9]')],
      employeeId:['', Validators.pattern('^[0-9]')],
      total:['', Validators.pattern('^[0-9]')],
      customer: this.fb.group({
        firstName: [''],
        lastName: [''],
        title: [''],
      }),
      shippingAddress:this.fb.group({
        city: [''],
        state:[''],
        zip:[''],
        street:[''],
      })
    })
  }



  setElapsed(duration:number | null){
    if (duration === null){
      this.elapsed.loading = true;
    } else {
      if (duration > 1000) {
        this.elapsed.loading = false;
        this.elapsed.duration = duration / 1000;
        this.elapsed.elapsedLabel = 'seconds';
      } else {
        this.elapsed.duration = duration;
        this.elapsed.loading = false;
        this.elapsed.elapsedLabel = 'milliseconds';
      }
    }
  }


  ngOnInit(): void {
    this.refresh();

  }

  setPage(newPage:number){
    if (newPage && newPage-1 !== this.data?.pageable.pageNumber) {
      this.refresh(newPage -1);
    }
  }




  refresh(newPage?: number){
    this.elapsed.loading = true;
    this.data = undefined;
    performance.mark(`getAllOrders-${this.service.useMongo}-started`)
    this.service.getAllOrders(this.searchForm.value, newPage).subscribe((data) => {
      this.data = data;
      performance.mark(`getAllOrders-${this.service.useMongo}-finished`)
      const result = performance.measure(`getAllOrders-${this.service.useMongo}-duration`,`getAllOrders-${this.service.useMongo}-started`,`getAllOrders-${this.service.useMongo}-finished` )
      this.setElapsed(result.duration);
    })

  }

  resetForm() {
    this.searchForm.reset(this.searchForm.value);
  }


}
