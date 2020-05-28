import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http'; // za HTTP requests
import { Observable } from 'rxjs';

import { User } from '../models/user'; // importuje user klasu koju smo napravili
import { Zakupnina } from '../models/zakupnina';
import { Skladiste } from '../models/skladiste';
import { SkladisnaJedinica } from '../models/skladisnaJedinica';


@Injectable()
export class UserService {
    private usersUrl: string; // atribut koji cuva http path
    private OAuthURL: string; // atribut koji cuva http path
    private secureUrl: string; // atribut koji cuva http path


constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8762/user/api/users'; // promijeniti na onaj URL koji nas vodi do korijena user API-ja
    this.secureUrl = 'http://localhost:8762/user/api/secure/users/';
    this.OAuthURL = 'http://localhost:8762/user/oauth/token';
}
    // sada redamo metode koje hocemo da servis ima

    // pronalazi sve zakupnine
    public findAllBillings(): Observable<Zakupnina[]> {
        let url = 'http://localhost:8762/user/api/secure/users/billings/';
        url += JSON.parse(window.sessionStorage.getItem('token')).client.id;
        return this.http.get<Zakupnina[]>(url);
      }

    public findStorage(id: number): Observable<Skladiste> {
        const url = 'http://localhost:8762/storage/api/skladista/' + id;
        const headers = {
          'Access-Control-Allow-Origin': 'http://localhost:4200'
        };

        return this.http.get<Skladiste>(url, {headers});
    }

    public addStorage(skladiste: Skladiste): Observable<any> {
      const url = 'http://localhost:8762/storage/api/skladista/';
      const headers = {
        'Content-type': 'application/json'
      };

      const body = {
        'adresa': skladiste.adresa,
        'brojJedinica': skladiste.broj_skladisnih_jedinica
      };

      return this.http.post<Skladiste>(url,body, {headers});
  }



    public deleteBilling(id: number): Observable<any> {
      const url = 'http://localhost:8762/billing/api/billings/' + id;
      return this.http.delete<any>(url);
    }

    // dodaje novog usera, ovo mozemo koristiti prilikom registracije novog korisnika
    public save(user: User) {


        const body = JSON.stringify(user);


        const headers = {
          'Content-type': 'application/json'
        };


        return this.http.post<User>(this.usersUrl, body, {headers});
      }

      // login pisati ovdje
      public login(user: User) {


        const body = new HttpParams()
        .set('username', user.mail)
        .set('password', user.password)
        .set('grant_type', 'password')
        .set('client-id', 'admin-client');

        const headers = {
          'Authorization': 'Basic ' + btoa('admin-client:admin'),
          'Content-type': 'application/x-www-form-urlencoded'
        };
        return this.http.post<User>(this.OAuthURL, body, {headers});
      }
}

/*
ovaj service se sada moze importovati u .ts od neke komponente, pa se metode servisa pozivaju u metodama komponente, a metode komponente
se vezu za stvari u HTML kodu komponente, npr.
U .ts kod za komponentu za registraciju (koju treba kreirati) se moze dodati ovo:
import { UserService } from '../services/user.service';
import { User } from '../models/user';

  pressRegistracija() {
    this.userService.save(this.user).subscribe(result => NESTO STO RADIMO SA REZULTATOM);
  }

  a onda u html kod za registraciju, kada pravimo formu:
  <form (ngSubmit)="pressRegistracija()" #userForm="ngForm"> znaci da se prilikom submit pozove metoda pressRegistracija koju smo napisali

  Gotov primjer ovoga ima u aplikaciji angularclient/src/app/user-form
*/
