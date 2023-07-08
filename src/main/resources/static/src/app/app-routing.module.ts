import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MovieComponent } from './movie/movie.component';
import { NowPlayingComponent } from './now-playing/now-playing.component';
import { RecommendendComponent } from './recommendend/recommendend.component';
import { AccountComponent } from './account/account.component';
import { HomeComponent } from './home/home.component';
import { RentalOrderComponent } from './rental-order/rental-order.component';
import { MovieInternalStorageComponent } from './movie-internal-storage/movie-internal-storage.component';
const routes: Routes = [
  { path: 'movie', component: MovieComponent },
  { path: 'now-playing', component: NowPlayingComponent },
  { path: 'recommended', component: RecommendendComponent },
  { path: 'account', component: AccountComponent },
  { path: 'rental-order', component: RentalOrderComponent }, //
  { path: 'movie-internal-storage', component: MovieInternalStorageComponent },
  { path: '', component: HomeComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
