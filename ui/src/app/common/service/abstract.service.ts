import {Injector} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {environment} from "../../../environments/environment";
import {HttpErrorResponse} from "@angular/common/http";
import {HttpHeaders} from "@angular/common/http";
import {throwError} from "rxjs";
import {catchError, retry} from "rxjs/internal/operators";
import { CookieService } from 'ngx-cookie-service';
import {CookieConstant} from "../constant/cookie-constant";
import {map} from "rxjs/internal/operators";

export abstract class AbstractService {

  /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
   | Private Variables
   |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
   | Protected Variables
   |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  // 기본 헤더
  protected httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  }

  // HTTP
  protected http: HttpClient;

  // Router
  protected router: Router;

  protected cookieService: CookieService;

  /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
   | Public Variables
   |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
   | Constructor
   |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  // 생성자
  protected constructor(protected injector: Injector) {

    this.router = injector.get(Router);
    this.http = injector.get(HttpClient);
    this.cookieService = injector.get(CookieService);
  }

  /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
   | Override Method
   |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
   | Public Method
   |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
   | Protected Method
   |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  /**
   * GET 호출
   *
   * @param {string} url
   * @param params
   * @returns {any}
   */
  protected get(url: string, params?: any): Observable<any> {

    console.log(`[${this[ '__proto__' ].constructor.name}] > GET ${url}`);
    console.log(`[${this[ '__proto__' ].constructor.name}] > PARAMS`, params);

    return this.http.get(url, this.getDefaultRequestOptions());
  }

  /**
   * POST 호출
   *
   * @param {string} url
   * @param data
   * @param {ContentType} contentType
   * @returns {Promise<any>}
   */
  protected post(url: string, data: any): Observable<any> {

    // console.log(`[${this[ '__proto__' ].constructor.name}] > POST ${url}`);
    // console.log(`[${this[ '__proto__' ].constructor.name}] > DATA`, data);
    // console.log(`[${this[ '__proto__' ].constructor.name}] > ContentType`, contentType);

    return this.http.post(url, data, this.getDefaultRequestOptions());
  }

  /**
   * PUT 호출
   *
   * @param {string} url
   * @param data
   * @returns {any}
   */
  protected put(url: string, data: any): Observable<any> {

    console.log(`[${this[ '__proto__' ].constructor.name}] > PUT ${url}`);
    console.log(`[${this[ '__proto__' ].constructor.name}] > DATA`, data);

    return this.http.put(url, data, this.getDefaultRequestOptions());
  }

  /**
   * DELETE 호출
   *
   * @param {string} url
   * @returns {any}
   */
  protected delete(url: string, params?: any): Observable<any> {

    console.log(`[${this[ '__proto__' ].constructor.name}] > DELETE ${url}`);

    return this.http.delete(url, this.getDefaultRequestOptions());
  }

  /**
   * Logout
   *
   * @returns {Promise<any>}
   */
  public logout(): Observable<any> {

    return this.delete(`${environment.host}/api/oauth/revoke`, null)
      .pipe(map(response => {
        console.info(response);
        return response;
      }));
  }

  /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
   | Private Method
   |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

  /**
   * 기본 옵션 정보 생성
   *
   * @returns {RequestOptions}
   */
  protected getDefaultRequestOptions() {

    const token = this.cookieService.get(CookieConstant.KEY.TOKEN);

    // 헤더
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    headers.set('Authorization', `Bearer ${token}`);

    // 옵션
    return { headers: headers, withCredentials: true };
  }

  /**
   * 기본 옵션 정보 생성(without token)
   *
   * @returns {RequestOptions}
   */
  protected getWithoutTokenRequestOptions() {

    // 헤더
    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    // 옵션
    return { headers: headers, withCredentials: true };
  }

}
