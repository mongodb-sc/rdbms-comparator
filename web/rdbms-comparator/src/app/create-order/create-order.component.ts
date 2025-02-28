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
import {Product, OrderDetail} from "../models/order_detail";
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
      customer:['', Validators.required],
      store:['', Validators.required],
      details: this.details
    })
  }

  ngOnInit() {

  }

  productFormatter = (x: { name: string, description: string }) => x.name + ' ' + x.description
  storeFormatter = (x: { name: string, region: string }) => x.name + ' ' + x.region
  customerFormatter = (x: { title: string, firstName: string, lastName: string }) => (x.title? x.title : ' ') + ' ' + (x.firstName? x.firstName: '') + ' ' + (x.lastName? x.lastName : '')
  inputFormatter =  (x: { name: string, description: string }) => x.name

  newOrderDetail():FormGroup  {
    return this.fb.group({
      quantity: ['', Validators.required],
      product:['', Validators.required]
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

  customerSearch = (searchTerm: Observable<string>) =>
      searchTerm.pipe(
          debounceTime(300),
          distinctUntilChanged(),
          switchMap(term =>
              this.service.getCustomerByName(term).pipe(
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

    let tempDetails:Array<OrderDetail> = [];
    for(let detail of this.orderForm.get('details')?.value){
      let product:Product = detail.product
      if (product.id == null) {
        product.id = detail.product.pg_id
      }
      let tmpDetail:OrderDetail = {product: product, quantity: detail.quantity}
      tempDetails.push(tmpDetail)
    }

    let customer = this.orderForm.get('customer')?.value
    if (customer.id == null){
      customer.id = customer.pg_id
    }

    const order: Order = {

      details: tempDetails,
      purchaseOrder: this.orderForm.get('purchaseOrder')?.value,
      customer: customer,
      store: this.orderForm.get('store')?.value,
      totalPieces: tempDetails.length,
      warehouseId: 1
    }
    this.service.createOrder(order).subscribe((data: Response<Order>)=> {
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
