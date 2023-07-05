export interface Movie {
  totalMovies : number;
  availableMovies : number;
  rentedMovies : number;
}

export interface MovieList {
  id: number;
  title: string;
  genres: Genre
  description: string;
  releaseDate: string;
  posterPath: string;
}

export interface Genre {
  id: number;
  name: string;
}
