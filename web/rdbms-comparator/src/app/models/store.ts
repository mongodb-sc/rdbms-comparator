import {Address} from "./address";

export interface Store {
    id: number;
    _id: string;
    managerName: string;
    region: string
    storeType: string
    sqFt: number,
    address: Address
}