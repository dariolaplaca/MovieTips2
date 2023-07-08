import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { MovieInStorage } from './movie-internal-storage/movie-in-storage';
@Injectable({
  providedIn: 'root'
})
export class MovieInternalStorageService {

  constructor(private http : HttpClient) { }
  private url: string = 'http://localhost:8080';

  getStorageData() {
    const finalurl = `${this.url}/api/movie/all`;
    return this.http.get(finalurl);
  }

  postNewMovie(responseBody: MovieInStorage, headers : HttpHeaders, params: HttpParams) {
    const finalurl = `${this.url}/api/movie/create`;
    this.http.post(finalurl, responseBody, {headers: headers, params: params}).subscribe(
      response => responseBody
    )
  }
}
