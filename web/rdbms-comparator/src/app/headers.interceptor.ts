import { HttpInterceptorFn } from '@angular/common/http';

export const headersInterceptor: HttpInterceptorFn = (req, next) => {
  const headersRequest = req.clone({
    headers: req.headers.set('Content-Type', 'application/json').set('Accept', 'application/json')
  })
  return next(headersRequest)



};
