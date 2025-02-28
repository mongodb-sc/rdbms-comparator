import {Customer} from "./customer";
import {Address} from "./address";
import {OrderDetail} from "./order_detail";
import {Store} from "./store";

export interface Order {
  id?:number
  orderDate?:Date;
  warehouseId:number
  fillDate?:Date
  purchaseOrder:string
  invoiceId?:string
  invoiceDate?:Date
  deliveryMethod?:string
  weight?:number
  totalPieces:number
  pickDate?:Date
  shippingMethod?:string
  billingDept?:number
  orderStatus?:string
  shippingStatus?:string
  deliveryDate?:Date
  orderType?: number
  employeeId?:number
  total?:number
  customer:Customer
  store: Store
  details:Array<OrderDetail>
  shippingAddress?:Address
}
