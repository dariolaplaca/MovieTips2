import { Component, OnInit } from '@angular/core';
import { Account } from './account';
import { AccountService } from '../account.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpHeaders, HttpParams } from '@angular/common/http';
import { DatePipe, getLocaleDateTimeFormat } from '@angular/common';
@Component({
  selector: 'mtips-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss']
})
export class AccountComponent implements OnInit {
  myService : AccountService;
  accountList: any;
  hide = true;

  constructor(private accountService: AccountService) {
    this.myService = accountService;
  }



  ngOnInit(): void {
    this.getInfo();
  }

  getInfo() {
    this.accountService.getAccountsData().subscribe(
      (response) => { this.accountList = response; },
      (error) => { console.log(error); });
  }

  register(): void {
    // Get form values
    const firstName = (document.getElementById('firstName') as HTMLInputElement).value;
    const surname = (document.getElementById('surname') as HTMLInputElement).value;
    const email = (document.getElementById('email') as HTMLInputElement).value;
    const password = (document.getElementById('password') as HTMLInputElement).value;
    const birthday = (document.getElementById('birthday') as HTMLInputElement).value;
    // Create the request body object
    var birthdayDate : any | null;
    if(birthday === null){
      birthdayDate = null;
    } else {

      birthdayDate = birthday
    }
    const requestBody = {
      name: firstName,
      surname: surname,
      email: email,
      password: password,
      birthday: birthdayDate,
      userRole: "USER"
    };

    // Set the HTTP headers
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + window.btoa("admin:password")
    });

    const username = "ADMIN";
    const params = new HttpParams()
      .set('username', username);

    this.accountService.postNewAccount(requestBody, headers, params);
  }

  addFavorite(){
    const accountid = (document.getElementById('account_id') as HTMLInputElement).value;
    const movieToFav = (document.getElementById('movie_to_favorite') as HTMLInputElement).value;
    var accountNumber = Number(accountid);
    var movieToFavId = Number(movieToFav);

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + window.btoa("admin:password")
    });

    const username : string = "ADMIN";
    const params = new HttpParams()
      .set('username', username);

    this.accountService.postNewFavorite(accountNumber, movieToFavId, headers, params);

  }

}
