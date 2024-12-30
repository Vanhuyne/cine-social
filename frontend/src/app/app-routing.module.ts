import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MovieListComponent } from './components/movie/movie-list/movie-list.component';
import { MovieDetailComponent } from './components/movie/movie-detail/movie-detail.component';
import { MovieGirdComponent } from './components/movie/movie-gird/movie-gird.component';

const routes: Routes = [
  { path: '', redirectTo: '/movies', pathMatch: 'full' },
  { path: 'movies', component: MovieListComponent },
  { path: 'movies/:id', component: MovieDetailComponent},
  { path: 'movies-gird', component: MovieGirdComponent},
  { path: '**', redirectTo: '/movies' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
