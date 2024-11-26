
import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {CustomerSearchComponent} from "./customer-search/customer-search.component";
import {OrderSearchComponent} from "./order-search/order-search.component";
import {ReportsComponent} from "./reports/reports.component";

const routes: Routes = [
  { path: "customers", component: CustomerSearchComponent, pathMatch: 'full'},
  { path: "orders", component: OrderSearchComponent, pathMatch: 'full'},
  { path: "reports", component: ReportsComponent, pathMatch: 'full'},
  {path: "**", redirectTo: "customers", pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
