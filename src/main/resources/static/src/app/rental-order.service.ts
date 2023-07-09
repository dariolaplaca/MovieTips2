import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class RentalOrderService {

  constructor(private http : HttpClient) { }
  private url: string = 'http://localhost:8080';

  getAllOrdersData() {
    const finalurl = `${this.url}/api/order/all`;
    return this.http.get(finalurl);
  }

  postNewOrder(headers : HttpHeaders, params : HttpParams) {
    const finalurl = `${this.url}/api/order/create`;
    this.http.post(finalurl, null, {headers: headers, params: params}).subscribe(
      response => console.log("OK")
    )
  }

  proceedCheckout(params: HttpParams){
    const finalurl = `${this.url}/api/order/checkout`;
    return this.http.get(finalurl, {params : params});
  }

  proceedFutureCheckout(params: HttpParams){
    const finalurl = `${this.url}/api/order/future-checkout`;
    return this.http.get(finalurl, {params : params});
  }
}
