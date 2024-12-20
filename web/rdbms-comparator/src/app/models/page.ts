export interface Page<T> {
  content: T;
  pageable: {
    pageNumber: number;
    pageSize: number;
  },
  totalElements:number;
  totalPages:number;
  size: number;
  queriesIssued: number;
  threadName:string;
  threadId:number;
  millis:number;
}
