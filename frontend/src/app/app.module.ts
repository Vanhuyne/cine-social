import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { MovieListComponent } from './components/movie/movie-list/movie-list.component';
import { MovieDetailComponent } from './components/movie/movie-detail/movie-detail.component';
import { MovieCardComponent } from './components/movie/movie-card/movie-card.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { MovieGirdComponent } from './components/movie/movie-gird/movie-gird.component';
import { SearchComponent } from './components/movie/search/search.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './components/login/login.component';
import { JwtModule } from '@auth0/angular-jwt';
import { AuthInterceptor } from './interceptor/auth-interceptor.interceptor';
import { RegisterComponent } from './components/register/register.component';
import { AdminDashbardComponent } from './components/admin/admin-dashbard/admin-dashbard.component';
import { UnauthorizedComponentComponent } from './components/unauthorized-component/unauthorized-component.component';
import { WatchListComponent } from './components/watch-list/watch-list.component';
import { CallbackComponent } from './components/callback/callback.component';
import { HeroComponent } from './components/hero/hero.component';
import { HomeComponent } from './components/home/home.component';
import { ReviewComponent } from './components/review/review.component';
import { ReviewModalComponent } from './components/review/review-modal/review-modal.component';
import { StartComponent } from './components/review/start/start.component';
import { ReviewRecentComponent } from './components/review/review-recent/review-recent.component';
import { ProfileComponent } from './components/profile/profile.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    MovieListComponent,
    MovieDetailComponent,
    MovieCardComponent,
    MovieGirdComponent,
    SearchComponent,
    LoginComponent,
    RegisterComponent,
    AdminDashbardComponent,
    UnauthorizedComponentComponent,
    WatchListComponent,
    CallbackComponent,
    HeroComponent,
    HomeComponent,
    ReviewComponent,
    ReviewModalComponent,
    StartComponent,
    ReviewRecentComponent,
    ProfileComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    JwtModule.forRoot({
      config: {
        tokenGetter() {
          return localStorage.getItem('access-token') || sessionStorage.getItem('access-token');
        },
      }
    })
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    provideClientHydration()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
