import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {LoginComponent} from "./login/login.component";
import {LoginGuard} from "./login/login.guard";
import {LoginForgotComponent} from "./login/login-forgot.component";


const routes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'login/forgot',
    component: LoginForgotComponent
  },
  {
    path: 'app',
    loadChildren: './layout/layout.module#LayoutModule',

  },
  {
    path: '**',
    redirectTo: 'app/error/404'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
