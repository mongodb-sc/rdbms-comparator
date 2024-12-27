import {Metrics} from "./metrics";

export interface Response<T> {
    content: T,
    threadName: string,
    threadId: number,
    millis: number,
    metrics: Array<Metrics>

}