import { Component, OnInit } from '@angular/core';
import { MovieInternalStorageService } from '../movie-internal-storage.service';
import { MovieInStorage } from './movie-in-storage';
import { HttpHeaders, HttpParams } from '@angular/common/http';

@Component({
  selector: 'mtips-movie-internal-storage',
  templateUrl: './movie-internal-storage.component.html',
  styleUrls: ['./movie-internal-storage.component.scss']
})
export class MovieInternalStorageComponent implements OnInit {

  movieStorage : any = [];

  constructor(private storageService : MovieInternalStorageService){  }

  ngOnInit(){
    this.getInfo();
  }
  getInfo() {
    this.storageService.getStorageData().subscribe(
      (response) => { this.movieStorage = response; },
      (error) => { console.log(error); });
  }

  addMovieToStorage(){
    const movieTitle = (document.getElementById('movie-title') as HTMLInputElement).value;
    const tmdbId = Number((document.getElementById('tmdb-id') as HTMLInputElement).value);
    const costPerDay = Number((document.getElementById('cost-per-day') as HTMLInputElement).value);
    const stockQuantity = Number((document.getElementById('stock-quantity') as HTMLInputElement).value);

    const requestBody = {
      name: movieTitle,
      tmdbId: tmdbId,
      costPerDay: costPerDay,
      stockQuantity: stockQuantity
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic ' + window.btoa("admin:password")
    });

    const username : string = "ADMIN";
    const params = new HttpParams()
      .set('username', username);

    this.storageService.postNewMovie(requestBody, headers, params);

  }
}
