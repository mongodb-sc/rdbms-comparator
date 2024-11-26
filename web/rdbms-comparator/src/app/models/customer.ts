import {Address} from "./address";
import { Email } from "./Email";
import {Phone} from "./Phone";

export interface Customer {
  firstName: string,
  lastName: string,
  title: string,
  address: Address,
  phones: Phone[];
  emails: Email[]

}
