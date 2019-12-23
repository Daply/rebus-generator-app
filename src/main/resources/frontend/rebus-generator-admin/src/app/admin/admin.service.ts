import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject, throwError } from 'rxjs';
import { Router } from '@angular/router';

@Injectable()
export class AdminService {

  baseUrl: string = "http://ec2-13-53-212-223.eu-north-1.compute.amazonaws.com:8080";
  headersValue = new HttpHeaders({
    'Content-Type': 'application/json',
    'Access-Control-Allow-Origin': this.baseUrl});
  options = { headers: this.headersValue };

  constructor (protected httpClient: HttpClient, private router: Router) {}

  getLanguages() {
    return this.httpClient.get<string[]>(this.baseUrl + '/api/langs', this.options);
  }

  loginAdmin(model: any) {
    let result = this.httpClient.post(this.baseUrl + "/login/admin", {
        username: model.username,
        password: model.password
    }).subscribe(isValid => {
        if (isValid) {
            sessionStorage.setItem(
              'auth',
              btoa(model.username + ':' + model.password)
            );
            this.router.navigate(['/main']);
        } else {
            alert("Authentication failed.");
        }
    });
  }

  uploadNewImage(data) {
    // const headers = new HttpHeaders({
    //   'Content-Type': 'application/json',
    //   'Access-Control-Allow-Origin': this.baseUrl,
    //    Authorization: 'Basic ' + sessionStorage.getItem('auth')});
    // return this.httpClient.post<any>(this.baseUrl + '/api/new/image', data, {headers});
    return this.httpClient.post<any>(this.baseUrl + '/api/new/image', data);
  }

}
