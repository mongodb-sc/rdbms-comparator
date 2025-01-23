
import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CustomerSearchComponent} from "./customer-search/customer-search.component";
import {OrderSearchComponent} from "./order-search/order-search.component";
import {ReportsComponent} from "./reports/reports.component";
import {CreateCustomerComponent} from "./create-customer/create-customer.component";
import {CreateOrderComponent} from "./create-order/create-order.component";

const routes: Routes = [
  { path: "customers",  children: [
      { path: "new", component: CreateCustomerComponent, pathMatch: 'full'},
      { path: "", component: CustomerSearchComponent, pathMatch: 'full'}
    ]},
  { path: "orders",  children: [
          { path: "new", component: CreateOrderComponent, pathMatch: 'full'},
          { path: "", component: OrderSearchComponent, pathMatch: 'full'}
  ]},
  { path: "reports", component: ReportsComponent, pathMatch: 'full'},
  {path: "**", redirectTo: "customers", pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
