import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {AppComponent} from "./app.component";
import {BrowserModule} from "@angular/platform-browser";
import {ReactiveFormsModule} from "@angular/forms";
import {NgbModule, NgbPaginationModule} from "@ng-bootstrap/ng-bootstrap";
import {RouterModule} from "@angular/router";
import {AppRoutingModule} from "./app-routing.module.tx";
import {provideHttpClient, withInterceptors} from "@angular/common/http";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { CustomerSearchComponent } from './customer-search/customer-search.component';
import { OrderSearchComponent } from './order-search/order-search.component';
import { ReportsComponent } from './reports/reports.component';
import {headersInterceptor} from "./headers.interceptor";



@NgModule({
  declarations: [
    AppComponent,
    CustomerSearchComponent,
    OrderSearchComponent,
    ReportsComponent,
  ],
  imports: [
    CommonModule,
    BrowserModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    NgbModule,
    NgbPaginationModule,
    RouterModule,
    AppRoutingModule
  ],
  providers:[provideHttpClient(withInterceptors([headersInterceptor]))],
  bootstrap: [AppComponent]
})
export class AppModule { }
