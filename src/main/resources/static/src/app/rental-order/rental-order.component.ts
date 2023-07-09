import { Component, OnInit } from '@angular/core';
import { RentalOrderService } from '../rental-order.service';
import { HttpHeaders, HttpParams, HttpStatusCode } from '@angular/common/http';

@Component({
  selector: 'mtips-rental-order',
  templateUrl: './rental-order.component.html',
  styleUrls: ['./rental-order.component.scss']
})
export class RentalOrderComponent implements OnInit {

  ordersList : any;
  checkout : any;
  futureCheckout : any;

  constructor(private rentalService: RentalOrderService) {
  }


  ngOnInit(): void {
    this.getAllOrdersData();
  }



  getAllOrdersData(){
    this.rentalService.getAllOrdersData().subscribe(
      (response) => {this.ordersList = response},
      (error) => { console.log(error); });
  }

  addNewOrder(){
    const movieId = Number((document.getElementById("movie-internal-id") as HTMLInputElement).value);
    const accountId = Number((document.getElementById("account-id") as HTMLInputElement).value);
    const rentalDays = Number((document.getElementById("rental-days") as HTMLInputElement).value);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + window.btoa("admin:password")
    });

    const username : string = "ADMIN";
    const params = new HttpParams()
    .set('movieID', movieId).set('rentalDays', rentalDays).set('accountId', accountId).set('username', username);

    this.rentalService.postNewOrder(headers, params);
  }

  proceedCheckout(){
    const orderId = Number((document.getElementById("order-id") as HTMLInputElement).value);
    const params = new HttpParams().set('rentalOrderId', orderId);
    this.rentalService.proceedCheckout(params).subscribe(
      (response : any) => {this.checkout = response.hasOwnProperty("message") ? response['message'] : response;},
      (error) => { if(error.error.message == "Order Not In Progress!"){
        this.checkout = error.error.message;
      }
        console.log(error); });
  }

  proceedFutureCheckout(){
    const orderId = Number((document.getElementById("order-id") as HTMLInputElement).value);
    const params = new HttpParams().set('rentalOrderId', orderId);
    this.rentalService.proceedFutureCheckout(params).subscribe(
      (response : any) => {
        this.futureCheckout = response.hasOwnProperty("message") ? response['message'] : response;},
      (error) => { if(error.error.message == "Order Not In Progress!"){
        this.futureCheckout = error.error.message;
      }
        console.log(error); });
  }
}
