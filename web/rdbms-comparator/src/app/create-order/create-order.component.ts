import {Component, OnInit, TemplateRef} from '@angular/core';
import {AppService} from "../app.service";
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {
  catchError,
  debounceTime,
  distinctUntilChanged,
  merge,
  Observable,
  of,
  OperatorFunction,
  switchMap,
  tap
} from "rxjs";
import {Product} from "../models/order_detail";
import {Response} from "../models/response";
import {Customer} from "../models/customer";
import {Order} from "../models/Order";
import {Query} from "../models/Query";
import {NgbOffcanvas} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-create-order',
  templateUrl: './create-order.component.html',
  styleUrl: './create-order.component.sass'
})
export class CreateOrderComponent implements OnInit{

  orderForm: FormGroup;
  details: FormArray;
  products?: Observable<Product[]>
  order?: Response<Order>
  queries?: Observable<Query[]>

  constructor(private fb:FormBuilder, private service:AppService, private offCanvasService: NgbOffcanvas) {

    this.details = new FormArray([this.newOrderDetail()])
    this.orderForm = this.fb.group({
      purchaseOrder:['', Validators.required],
      customer_id:['', Validators.required],
      store_id:['', Validators.required],
      details: this.details
    })
  }

  ngOnInit() {

  }

  productFormatter = (x: { name: string, description: string }) => x.name + ' ' + x.description
  storeFormatter = (x: { name: string, region: string }) => x.name + ' ' + x.region
  inputFormatter =  (x: { name: string, description: string }) => x.name

  newOrderDetail():FormGroup  {
    return this.fb.group({
      quantity: ['', Validators.required],
      product_id:['', Validators.required]
    })

  }

  productSearch = (searchTerm: Observable<string>) =>
  searchTerm.pipe(
      debounceTime(300),
      distinctUntilChanged(),
      switchMap(term =>
          this.service.getProductList(term).pipe(
            catchError(e => {
              return of([]);
            }))
        )
  );

  storeSearch = (searchTerm: Observable<string>) =>
      searchTerm.pipe(
          debounceTime(300),
          distinctUntilChanged(),
          switchMap(term =>
              this.service.getStores(term).pipe(
                  catchError(e => {
                    return of([]);
                  }))
          )
      );

  addItem():void {
    this.details.push(this.newOrderDetail());
  }

  save(): void {
    this.service.createOrder(this.orderForm.value).subscribe((data: Response<Order>)=> {
      this.order = data;
    })
  }

  open(content: TemplateRef<any>) {
    if (this.order) {
      this.queries = this.service.getQueries(this.order?.threadName, this.order?.threadId, this.order?.millis);
    }
    this.offCanvasService.open(content, { position: 'bottom', ariaLabelledBy: 'offcanvas-basic-title' }).result.then(
    );

  }
}
