import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {

  constructor() { }

  public getToken(): string 
  {
    return JSON.parse(window.sessionStorage.getItem('token')).access_token;
  }  
}