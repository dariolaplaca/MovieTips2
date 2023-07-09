import { Movie } from "../movie/movie";
import { Account } from "../account/account";
import { MovieInStorage } from "../movie-internal-storage/movie-in-storage";

export interface RentalOrder {
  id : number;
  movie : MovieInStorage;
  account : Account;
  orderTime : Date;
  returnTime : Date;
  orderStatus : string;
}
