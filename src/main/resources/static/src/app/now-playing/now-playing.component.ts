import { Component, OnInit } from '@angular/core';
import { Movie } from '../movie/movie';
import { MovieService } from '../movie.service';

@Component({
  selector: 'mtips-now-playing',
  templateUrl: './now-playing.component.html',
  styleUrls: ['./now-playing.component.scss']
})
export class NowPlayingComponent implements OnInit {
  movieList : any = [];
  isHidden : boolean = true;
  constructor (private movieService: MovieService) { }

  ngOnInit(){
    this.movieService.getNowPlaying().subscribe(
      (response) => { this.movieList = response; },
      (error) => { console.log(error); });
  }

  toggleHide() : void{
    this.isHidden = !this.isHidden;
  }

}
