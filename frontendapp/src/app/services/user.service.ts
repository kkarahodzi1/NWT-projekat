import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http'; // za HTTP requests
import { Observable } from 'rxjs/Observable';

import { User } from '../models/user'; // importuje user klasu koju smo napravili

@Injectable()
export class UserService {
    private usersUrl: string; // atribut koji cuva http path
constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8762/users'; // promijeniti na onaj URL koji nas vodi do korijena user API-ja
}
    // sada redamo metode koje hocemo da servis ima

    // pronalazi sve usere (vjerovatno nam nece trebati ali eto kao primjer)
    public findAll(): Observable<User[]> {
        // ako je potrebno, prvo prilagoditi tacan URL (paziti da se ne promijeni privatni atribut, on uvijek ostaje base URL)
        return this.http.get<User[]>(this.usersUrl);
      }

    // dodaje novog usera, ovo mozemo koristiti prilikom registracije novog korisnika
    public save(user: User) {
        // isto vazi kao i gore, prvo prilagoditi URL i uraditi neke provjere itd, i onda na kraju raditi post
        return this.http.post<User>(this.usersUrl, user);
      }

      // login pisati ovdje
      public login(user: User){
        console.log('RADIM LOGIN');
        return user;
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
