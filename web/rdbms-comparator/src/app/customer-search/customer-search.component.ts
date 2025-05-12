import {Component, Input, OnInit, TemplateRef} from '@angular/core';
import {AppService} from "../app.service";
import {BehaviorSubject, Observable} from "rxjs";
import {Customer} from "../models/customer";
import {reportUnhandledError} from "rxjs/internal/util/reportUnhandledError";
import {FormBuilder, FormGroup} from "@angular/forms";
import {Page} from "../models/page";
import {NgbOffcanvas} from "@ng-bootstrap/ng-bootstrap";
import {Query} from "../models/Query";
import {Order} from "../models/Order";
import {Response} from "../models/response";

@Component({
  selector: 'app-customer-search',
  templateUrl: './customer-search.component.html',
  styleUrl: './customer-search.component.sass'
})
export class CustomerSearchComponent implements OnInit{

  data?: Page<Customer[]>;
  queries?: Observable<Query[]>;
  recent: Array<Observable<Response<Order[]>>> = [];
  elapsed = {
    duration:  0,
    elapsedLabel: '',
    loading:false
  }
  showSimpleSearch = false;
  showComplexSearch =true;
  rowCollapsed: Array<boolean> = new Array<boolean>(100);

  searchForm: FormGroup;
  useMongo:BehaviorSubject<boolean>;

  constructor(private service: AppService, private fb:FormBuilder, private offCanvasService: NgbOffcanvas) {
    this.useMongo = this.service.useMongoObservable;
    this.rowCollapsed.fill(true);
    this.searchForm = this.fb.group({
      searchTerm:[''],
      firstName: [''],
      lastName: [''],
      title: [''],
      city: [''],
      state:[''],
      zip:[''],
      street:[''],
      phone: this.fb.group({
        type: [''],
        number:[''],
      }),
      email: this.fb.group({
        type: [''],
        email: ['']
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
    performance.mark(`GetAllCustomers-${this.service.useMongo}-started`)
    this.service.getAllCustomers(this.searchForm.value, newPage).subscribe((data) => {
      this.data = data;
      performance.mark(`GetAllCustomers-${this.service.useMongo}-finished`)
      const result = performance.measure(`GetAllCustomers-${this.service.useMongo}-duration`, `GetAllCustomers-${this.service.useMongo}-started`, `GetAllCustomers-${this.service.useMongo}-finished`)
      this.setElapsed(result.duration);
    })
  }

  resetForm() {
    this.searchForm.reset();
  }

  open(content: TemplateRef<any>) {
    if (this.data) {
      this.queries = this.service.getQueries(this.data?.threadName, this.data?.threadId, this.data?.millis);
    }
    this.offCanvasService.open(content, { position: 'bottom', ariaLabelledBy: 'offcanvas-basic-title' }).result.then(
    );

  }

  toggleSearch(){
    this.showSimpleSearch = !this.showSimpleSearch;
    this.showComplexSearch = !this.showComplexSearch;
  }

  collapseRow(index: number, customerId: number){
    this.rowCollapsed[index] = !this.rowCollapsed[index]
    if (!this.rowCollapsed[index]) { // only call if they are opening the row, not on closing
      this.recent[index] = this.service.getRecentOrders(customerId);
    }

  }



}
