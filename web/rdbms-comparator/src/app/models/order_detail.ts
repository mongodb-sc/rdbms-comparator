export interface OrderDetail {
  quantity:number
  product:Product

}

export interface Product {
  id:number
  name:string
  dept:number
  price:number
  description:string
}
