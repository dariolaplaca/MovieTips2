import { Component, OnInit } from '@angular/core';
import { Movie } from './movie';
import { MovieService } from '../movie.service';

@Component({
  selector: 'mtips-movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.scss']
})
export class MovieComponent implements OnInit {

  tmdb_id: number = 0;
  movieTitle: string = "";
  movieTitles : any[] = [];
  movieById : any;
  movieByName : any;
  isHidden : boolean = true;

  constructor (private movieService: MovieService) { }

  ngOnInit() : void {
  }

  getInfo(){
    let myValue = (<HTMLInputElement>document.getElementById("tmdbId")).value;
    this.tmdb_id = <number><unknown>myValue;

    this.movieService.getMovieData(this.tmdb_id).subscribe(
      (response) => { this.movieById = response; },
      (error) => { console.log(error); });
  }

  getInfoByName(){
    let myValue = (<HTMLInputElement>document.getElementById("movieName")).value;
    this.movieTitle = myValue;

    this.movieService.getMovieDataByName(this.movieTitle).subscribe(
      (response) => { this.movieByName = response; },
      (error) => { console.log(error); });
  }

  toggleHide() : void{
    this.isHidden = !this.isHidden;
  }
}
