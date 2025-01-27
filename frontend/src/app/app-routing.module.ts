import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { MovieListComponent } from './components/movie/movie-list/movie-list.component';
import { MovieDetailComponent } from './components/movie/movie-detail/movie-detail.component';
import { AuthGuard } from './guards/auth.guard';
import { AdminDashbardComponent } from './components/admin/admin-dashbard/admin-dashbard.component';
import { WatchListComponent } from './components/watch-list/watch-list.component';
import { CallbackComponent } from './components/callback/callback.component';
import { MovieGirdComponent } from './components/movie/movie-gird/movie-gird.component';
import { HomeComponent } from './components/home/home.component';

const routes: Routes = [
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  {path: 'home', component: HomeComponent},
  { path: 'movies', component: MovieListComponent },
  { path: 'movie/:movieId', component: MovieDetailComponent },
  { path: 'admin-dashbard', component: AdminDashbardComponent, canActivate: [AuthGuard], data: { roles: ['ROLE_ADMIN'] } },
  { path: 'watch-list', component: WatchListComponent ,canActivate: [AuthGuard] },
  { path: 'explore', component: HomeComponent },
  { path: 'login-callback', component: CallbackComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
