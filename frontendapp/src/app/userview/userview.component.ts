import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { Zakupnina } from '../models/zakupnina';
import { Skladiste } from '../models/skladiste';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { promise } from 'protractor';
import { SkladisnaJedinica } from '../models/skladisnaJedinica';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Tip } from '../models/tip';


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
  sveJedinice: SkladisnaJedinica[];
  zakup: Zakupnina;
  formaDodaj: FormGroup;

  constructor(private formBuilder: FormBuilder,private userService: UserService, private router: Router, private toastr: ToastrService) {
    this.svaSkladista = new Array<Skladiste>();
    this.sveZakupnine = new Array<Zakupnina>();
    this.sveJedinice = new Array<SkladisnaJedinica>();
    
    this.formaDodaj = this.formBuilder.group({
      skladiste: [''],
      jedinica: [''],
      datum: new Date()
    });
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

  daLiJeObrisan(data: Zakupnina) {
    if (data.obrisan === true) {
      return false;
    } else {
      return true;
    }
  }

  dodajZakupnine() {
    this.userService.findAllBillings().subscribe(data => {
      this.sveZakupnine = data.filter(this.daLiJeObrisan);
    }, error => {
      this.toastr.error('Greška pri učitavanju', 'GREŠKA');
    });
  }

  obrisiZakupninu(id: number) {
    this.userService.deleteBilling(id).subscribe(data => {
      this.toastr.success('Zakupnina obrisana!', 'USPJEH');
      this.router.navigate(['userview']);
      this.lista = false;
    });
  }

  dobaviSkladista(){

    this.userService.findAllStorages().subscribe(data => {
      this.svaSkladista = data;
    },
    error => {
      this.toastr.error('Greška pri učitavanju', 'GREŠKA');
    });
    
  }

  onChange(skladData)
  {
    this.userService.findAllUnits(skladData.skladiste).subscribe(data=>
      {
        this.sveJedinice = data;
        this.formaDodaj.controls['jedinica'].setValue(this.sveJedinice[0]);
      },
      error => {
        this.toastr.error('Greška pri učitavanju', 'GREŠKA');
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
    this.dobaviSkladista();
  }

  dodajZakupninu(dodajZakup){

    if (dodajZakup.jedinica == "" || dodajZakup.jedinica == null ||
        dodajZakup.skladiste == "" || dodajZakup.skladiste == null ||
        dodajZakup.datum == "" || dodajZakup.datum == null) {
      this.toastr.warning('Niste unijeli sve podatke', 'UPOZORENJE');
      return;
    }
    
    const danas = new Date();
    const rezervacija = new Date(dodajZakup.datum)
    const brojMjeseci = (rezervacija.getTime() - danas.getTime())/(3600*24*30*1000);
    if (brojMjeseci < 1){
      this.toastr.warning('Datum isteka rezervacije mora biti barem 30 dana od danasnjeg datuma', 'UPOZORENJE');
      return;
    }

    const zakup = new Zakupnina();
    zakup.datum_sklapanja_ugovora = this.dateAsYYYYMMDDHHNNSS(danas);
    zakup.datum_raskida_ugovora = this.dateAsYYYYMMDDHHNNSS(rezervacija);
    zakup.skladisteId = dodajZakup.skladiste.id;
    zakup.jedinicaId = dodajZakup.jedinica.id;
    zakup.korisnikId = this.korisnik.id;
    zakup.ukupnaCijena = brojMjeseci*dodajZakup.jedinica.tip.cijena;
    
    this.userService.addBilling(zakup).subscribe(data => {
      this.toastr.success('Uspješno dodana zakupnina', 'USPJEH');
    }, error => {
      this.toastr.error('Zakupnina nije dodana', 'GREŠKA');
    });

    this.formaDodaj.reset();
  }

  dateAsYYYYMMDDHHNNSS(date): string {
    return date.getFullYear()
              + '-' + this.leftpad(date.getMonth() + 1, 2)
              + '-' + this.leftpad(date.getDate(), 2)
              + ' ' + this.leftpad(date.getHours(), 2)
              + ':' + this.leftpad(date.getMinutes(), 2)
              + ':' + this.leftpad(date.getSeconds(), 2);
  }
  
  leftpad(val, resultLength = 2, leftpadChar = '0'): string {
    return (String(leftpadChar).repeat(resultLength)
          + String(val)).slice(String(val).length);
  }
}
