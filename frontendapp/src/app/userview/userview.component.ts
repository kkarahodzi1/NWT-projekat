import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { Zakupnina } from '../models/zakupnina';
import { Skladiste } from '../models/skladiste';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { promise } from 'protractor';
import { SkladisnaJedinica } from '../models/skladisnaJedinica';
import { FormBuilder } from '@angular/forms';
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
  formaDodaj;
  sviTipovi:Tip[];


  constructor(private formBuilder: FormBuilder,private userService: UserService, private router: Router, private toastr: ToastrService) {
    this.svaSkladista = new Array<Skladiste>();
    this.sveZakupnine = new Array<Zakupnina>();
    this.sveJedinice = new Array<SkladisnaJedinica>();
    this.sviTipovi = new Array<Tip>();
    
    this.formaDodaj = this.formBuilder.group({
      skladise: [''],
      jedinica: [''],
      datum: new Date()
    });
  }

  ngOnInit() {
    this.lista = false;
    this.nova = false;
    // if (window.sessionStorage.getItem('token') == null) {
    //   this.toastr.error('Korisnik nije prijavljen!', 'GREŠKA');
    //   this.router.navigate(['']);
    // } else if (JSON.parse(window.sessionStorage.getItem('token')).client.role === 1) {
    //   this.router.navigate(['adminview']);
    // }
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
  }

  dodajZakupninu(dodajZakup){

    if (dodajZakup.jedinica == "" || dodajZakup.jedinica == null ||
        dodajZakup.skladiste == "" || dodajZakup.skladiste == null ||
        dodajZakup.datum == "" || dodajZakup.datum == null) {
      this.toastr.warning('Niste unijeli sve podatke', 'UPOZORENJE');
      return;
    }

    this.userService.findAllTypes().subscribe(data => {
      this.sviTipovi = data;
    },
    error => {
      this.toastr.error('Greška pri učitavanju', 'GREŠKA');
    });

    const tip = this.sviTipovi.find(x => x.tip_id == dodajZakup.jedinica.tip);
    const danas = new Date();
    const brojMjeseci = (dodajZakup.datum.getTime() - danas.getTime())/(3600*24*30*1000);

    if (brojMjeseci < 30){
      this.toastr.warning('Datum isteka rezervacije mora biti barem 30 dana od danasnjeg datuma', 'UPOZORENJE');
      return;
    }

    const zakup = new Zakupnina();
    zakup.datum_sklapanja_ugovora = danas.toLocaleString();
    zakup.datum_raskida_ugovora = dodajZakup.datum;
    zakup.skladisteId = dodajZakup.skladiste.id;
    zakup.jedinicaId = dodajZakup.jedinica.id;
    zakup.korisnikId = 0;
    zakup.ukupnaCijena = brojMjeseci*tip.cijena;
    console.log(zakup);
    
    // this.userService.addBilling(zakup).subscribe(data => {
    //   this.toastr.success('Uspješno dodana zakupnina', 'USPJEH');
    // }, error => {
    //   this.toastr.error('Zakupnina nije dodana', 'GREŠKA');
    // });

    this.formaDodaj.reset();
  }
}
