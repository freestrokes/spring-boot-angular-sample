import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor, HttpErrorResponse
} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Router} from "@angular/router";
import {catchError} from "rxjs/internal/operators";
import {UserService} from "../../layout/user/user.service";

@Injectable()
export class HttpErrorInterceptor implements HttpInterceptor {

  constructor(private router: Router, private userService: UserService) {
  }


  /**
   * intercept all XHR request
   * @param request
   * @param next
   * @returns {Observable<A>}
   */
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    /**
     * continues request execution
     */
    return next.handle(request).pipe(catchError((error, caught) => {
      //intercept the respons error and displace it to the console
      console.log(error);
      this.handleAuthError(error);
      return of(error);
    }) as any);
  }


  /**
   * manage errors
   * @param err
   * @returns {any}
   */
  private handleAuthError(error: HttpErrorResponse): Observable<any> {
    //handle your auth error or rethrow
    switch (error.status) {
      case 500: {

        this.router.navigate([ 'app/error/500' ]);

        break;
      }
      case 400: {

        console.log(`[${this[ '__proto__' ].constructor.name}] > INVALID_REQUEST`);

        break;
      }
      case 401: {

        console.log(`[${this[ '__proto__' ].constructor.name}] > UNAUTORIZED`);

        this.userService.logout();
        this.router.navigate([ 'app/error/401' ]);

        break;
      }
      case 403: {
        this.router.navigate([ 'app/error/403' ]);

        break;
      }
      default: {

        console.log(`[${this[ '__proto__' ].constructor.name}] > (SERVER_ERROR|INVALID_REQUEST|UNAUTORIZED|FORBIDDEN) ELSE ERROR`);

        this.router.navigate([ 'app/error/500' ]);

        break;
      }
    }
    throw error;
  }
}
