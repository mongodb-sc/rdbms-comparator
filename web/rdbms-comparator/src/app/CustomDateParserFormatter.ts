import {NgbDateParserFormatter, NgbDateStruct} from "@ng-bootstrap/ng-bootstrap";
import {Injectable} from "@angular/core";

@Injectable()
export class CustomDateParserFormatter extends NgbDateParserFormatter {

    readonly DELIMITER = '-';

    parse(value: string): NgbDateStruct | null {
        if (value) {
            const separator=value.indexOf(".")>=0?".":value.indexOf("-")>=0?"-":value.indexOf("/")>=0?"/":null
            if (separator)
            {
                let date = value.split(separator);
                return {
                    day : parseInt(date[2], 10),
                    month : parseInt(date[1], 10),
                    year : parseInt(date[0], 10)
                };
            }
        }
        return null;
    }

    format(date: NgbDateStruct | null): string {
        return date ? date.year+ this.DELIMITER+('00'+date.month).slice(-2) + this.DELIMITER + ('00'+date.day).slice(-2)   : "";
    }
}