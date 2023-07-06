import { Injectable } from '@angular/core';
import { Movie, MovieList } from './movie/movie';
import { HttpClient, HttpClientModule, HttpHandler } from '@angular/common/http';
@Injectable({
  providedIn: 'root'
})
export class MovieService {

  private url: string = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getMovieData(movie_id: number) {
    const finalurl = `${this.url}/api/movie/tmdb/id/${movie_id}`;
    return this.http.get(finalurl);
  }

  getNowPlaying() {
    const finalurl = `${this.url}/api/movie/now-playing`
    return this.http.get(finalurl);
  };

  getRecommended(account_id: number) {
    const finalurl = `${this.url}/api/favorites/recommended-movies/${account_id}`;
    return this.http.get(finalurl);
  }
}

