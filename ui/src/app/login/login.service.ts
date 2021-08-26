import {Injectable, Injector} from '@angular/core';
import {AbstractService} from "../common/service/abstract.service";
import {environment} from "../../environments/environment";
import {CookieConstant} from "../common/constant/cookie-constant";
import {Observable} from "rxjs";
import {HttpHeaders} from "@angular/common/http";
import {map, tap} from "rxjs/internal/operators";

/**
 * Login 서비스
 */
@Injectable()
export class LoginService extends AbstractService {

	/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Private Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

	/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Protected Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

	/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Public Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

	/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Constructor
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

	constructor(protected injector: Injector) {
		super(injector);
	}

	/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Override Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

	/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Public Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  public login(userId: string, password: string): Observable<any> {

    // 헤더
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': 'Basic cmVnaXN0cnk6cmVnaXN0cnktc2VjcmV0'
    });

    // 옵션
    const options = { headers: headers };

    return this.http
      .post(`${environment.apiUrl}/oauth/token?grant_type=password&scope=read&username=${userId}&password=${password}`, null, options)
      .pipe(
        map(response => {
          this.cookieService.set(CookieConstant.KEY.TOKEN, response['access_token'], null, '/');
          return response;
        })
      );
  }

  public loginCheck(): Observable<any> {
    // 호출
    return this.get(`${environment.apiUrl}/user/`, this.getDefaultRequestOptions());
  }

	/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	| Protected Method
	|-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

	/*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Private Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

}
