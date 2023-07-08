import { Injectable } from '@angular/core';
import { HttpClient, HttpClientModule, HttpHandler, HttpHeaders, HttpParams } from '@angular/common/http';
import { Account } from './account/account';
import { AccountComponent } from './account/account.component';
import { Observable, Subject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AccountService {


  private url: string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }
  getAccountsData() {
    const finalurl = `${this.url}/api/account/all`;
    return this.http.get(finalurl);
  }

  postNewAccount(responseBody: Account, headers: HttpHeaders, params: HttpParams) {
    const finalurl = `${this.url}/api/account/create`;
    this.http.post(finalurl, responseBody, {headers: headers, params: params}).subscribe(
      response => responseBody
    );
  }

  postNewFavorite(account_id : number, movie_id : number, headers: HttpHeaders, params: HttpParams) {
    const finalurl = `${this.url}/api/favorites/${account_id}/id/${movie_id}`;
    this.http.post(finalurl, null, {headers: headers, params: params}).subscribe(
      response => {
        console.log("OK")
      });
  }
}
