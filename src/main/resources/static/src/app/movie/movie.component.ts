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
  movie : any;

  constructor (private movieService: MovieService) { }

  ngOnInit() : void {
  }

  toggle(){
    let myValue = (<HTMLInputElement>document.getElementById("tmdbId")).value;
    this.tmdb_id = <number><unknown>myValue;

    this.movieService.getMovieData(this.tmdb_id).subscribe(
      (response) => { this.movie = response; },
      (error) => { console.log(error); });
  }

}
