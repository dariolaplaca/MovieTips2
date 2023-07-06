import { Component } from '@angular/core';
import { MovieService } from '../movie.service';

@Component({
  selector: 'mtips-recommendend',
  templateUrl: './recommendend.component.html',
  styleUrls: ['./recommendend.component.scss']
})
export class RecommendendComponent {
  account_id: number = 0;
  movieList : any = [];
  isHidden : boolean = true;

  constructor (private movieService: MovieService) { }

  ngOnInit() : void {
  }

  getRecommended(){
    let myValue = (<HTMLInputElement>document.getElementById("account_id")).value;
    this.account_id = <number><unknown>myValue;

    this.movieService.getRecommended(this.account_id).subscribe(
      (response) => { this.movieList = response; },
      (error) => { console.log(error); });
  }

  toggleHide() : void{
    this.isHidden = !this.isHidden;
  }

}
