import {Component, OnInit, TemplateRef} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AppService} from "../app.service";
import {Observable} from "rxjs";
import {Customer} from "../models/customer";
import {Response} from "../models/response";
import {Query} from "../models/Query";
import {NgbOffcanvas} from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: 'app-create-customer',
  templateUrl: './create-customer.component.html',
  styleUrl: './create-customer.component.sass'
})
export class CreateCustomerComponent implements OnInit{

  customerForm: FormGroup;
  queries?: Observable<Query[]>
  phones: FormArray;
  emails: FormArray;
  showCloseButton = false;
  customer?:Response<Customer>

  constructor(private fb: FormBuilder, private service: AppService, private offCanvasService: NgbOffcanvas) {

    this.phones = new FormArray([this.newPhone()])
    this.emails = new FormArray([this.newEmail()])


    this.customerForm = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      title: [''],
      address: this.fb.group({
        city: ['', Validators.required],
        state: ['', Validators.required],
        zip: ['',Validators.required],
        street: ['',Validators.required]
      }),
      phones: this.phones,
      emails: this.emails
    })
  }


  newPhone():FormGroup  {
    return this.fb.group({
      type: ['', Validators.required],
      number:['', Validators.required]
    })

  }

  addPhone():void {
    this.phones.push(this.newPhone());
  }

  addEmail():void {
    this.emails.push(this.newEmail());
  }

  newEmail():FormGroup  {
   return this.fb.group({
      type: ['', Validators.required],
      email:['', [Validators.required, Validators.email]]
    })
  }



  ngOnInit() {
  }

  save():void {

    this.service.createCustomer(this.customerForm.value).subscribe((data: Response<Customer>)=> {
      this.customer = data;
      this.showCloseButton = true;
    })
  }

  resetForm(){
    this.customerForm.reset();

  }

  open(content: TemplateRef<any>) {
    if (this.customer) {
      this.queries = this.service.getQueries(this.customer?.threadName, this.customer?.threadId, this.customer?.millis);
    }
    this.offCanvasService.open(content, { position: 'bottom', ariaLabelledBy: 'offcanvas-basic-title' }).result.then(
    );

  }

}
