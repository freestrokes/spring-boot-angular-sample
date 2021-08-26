import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {SharedModule} from "./common/shared.module";
import { CookieService } from 'ngx-cookie-service';
import {LoginComponent} from "./login/login.component";
import {LoginForgotComponent} from "./login/login-forgot.component";
import {LoginService} from "./login/login.service";
import {UserService} from "./layout/user/user.service";
import {HttpErrorInterceptor} from "./common/service/error.interceptor";
import {HTTP_INTERCEPTORS} from "@angular/common/http";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    LoginForgotComponent
  ],
  imports: [
    SharedModule,
    BrowserModule,
    AppRoutingModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorInterceptor,
      multi: true,
    },
    CookieService,
    LoginService,
    UserService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
