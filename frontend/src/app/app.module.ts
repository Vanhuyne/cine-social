import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './components/layout/header/header.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { MovieListComponent } from './components/movie/movie-list/movie-list.component';
import { MovieDetailComponent } from './components/movie/movie-detail/movie-detail.component';
import { MovieCardComponent } from './components/movie/movie-card/movie-card.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { MovieGirdComponent } from './components/movie/movie-gird/movie-gird.component';
import { SearchComponent } from './components/movie/search/search.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './components/layout/login/login.component';
import { JwtModule } from '@auth0/angular-jwt';
import { AuthInterceptor } from './interceptor/auth-interceptor.interceptor';
import { ClickOutsideDirectiveDirective } from './directive/click-outside-directive.directive';

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
    ClickOutsideDirectiveDirective
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
