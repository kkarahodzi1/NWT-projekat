import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { Zakupnina } from '../models/zakupnina';
import { Skladiste } from '../models/skladiste';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { promise } from 'protractor';


@Component({
  selector: 'app-userview',
  templateUrl: './userview.component.html',
  styleUrls: ['./userview.component.css']
})
export class UserviewComponent implements OnInit {
  korisnik: User;
  lista: boolean;
  nova: boolean;
  sveZakupnine: Zakupnina[];
  svaSkladista: Skladiste[];


  constructor(private userService: UserService, private router: Router, private toastr: ToastrService) {
    this.svaSkladista = new Array<Skladiste>();
    this.sveZakupnine = new Array<Zakupnina>();
  }

  ngOnInit() {
    this.lista = false;
    this.nova = false;
    if (window.sessionStorage.getItem('token') == null) {
      this.toastr.error('Korisnik nije prijavljen!', 'GREŠKA');
      this.router.navigate(['']);
    } else if (JSON.parse(window.sessionStorage.getItem('token')).client.role === 1) {
      this.router.navigate(['adminview']);
    }
    this.korisnik = JSON.parse(window.sessionStorage.getItem('token')).client;
  }

  daLiJeObrisan(data: Zakupnina)
  {
    if(data.obrisan === true) {
      return false;
    } else {
      return true;
    }
  }

   dodajZakupnine() {
    this.userService.findAllBillings().subscribe(data => {
      this.sveZakupnine = data.filter(this.daLiJeObrisan);
    }, error => {
      this.toastr.error('Greška pri učitavanju', 'GREŠKA' );
    });
  }

  obrisiZakupninu(id: number) {
    this.userService.deleteBilling(id).subscribe(data => {
      this.toastr.success('Zakupnina obrisana!', 'USPJEH' );
      this.router.navigate(['userview']);
      this.lista=false;
    });
  }

    izlistaj() {
    this.svaSkladista = new Array<Skladiste>();
    this.sveZakupnine = new Array<Zakupnina>();
    this.lista = true;
    this.nova = false;
    this.dodajZakupnine();
  }

  dodaj() {
    this.lista = false;
    this.nova = true;
  }


}
