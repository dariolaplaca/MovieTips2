import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MovieComponent } from './movie/movie.component';
import { HttpClientModule } from '@angular/common/http';
import { NowPlayingComponent } from './now-playing/now-playing.component';
import { RecommendendComponent } from './recommendend/recommendend.component';

@NgModule({
  declarations: [
    AppComponent,
    MovieComponent,
    NowPlayingComponent,
    RecommendendComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
