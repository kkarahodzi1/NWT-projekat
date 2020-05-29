import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { Zakupnina } from '../models/zakupnina';
import { Skladiste } from '../models/skladiste';
import { Tip } from '../models/tip';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { promise } from 'protractor';
import { FormBuilder } from '@angular/forms';
import {MatMenuModule} from '@angular/material/menu';


@Component({
  selector: 'app-adminview',
  templateUrl: './adminview.component.html',
  styleUrls: ['./adminview.component.css']
})
export class AdminviewComponent implements OnInit {
  korisnik: User;
  dodavanjeSklad: boolean;
  dodavanjeSJed: boolean;
  forma;
  forma2;
  svaSkladista: Skladiste[];
  sviTipovi: Tip[];

  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router, private toastr: ToastrService) {
    this.forma = this.formBuilder.group({
      adresa: '',
      kapacitet: ''
    });
    this.forma2 = this.formBuilder.group({
      inputBrojJed: '',
      skladChoose: [''],
      tipChoose: ['']
    });
    this.svaSkladista = new Array<Skladiste>();
    this.sviTipovi = new Array<Tip>();
    //this.toastr.toastrConfig.positionClass = 'toast-top-center'
  }

  ngOnInit() {
    this.dodavanjeSklad = false;
    this.dodavanjeSJed = false;
    if (window.sessionStorage.getItem('token') == null) {
      this.toastr.error('Korisnik nije prijavljen!', 'GREŠKA');
      this.router.navigate(['']);
    } else if (JSON.parse(window.sessionStorage.getItem('token')).client.role === 0) {
      this.router.navigate(['userview']);
    }
    this.korisnik = JSON.parse(window.sessionStorage.getItem('token')).client;
  }

  dodajSkladiste()
  {
    this.dodavanjeSJed = false;
    this.dodavanjeSklad = true;
  }

  postSkladiste(skladData)
  {
    var skladiste = new Skladiste;
    skladiste.adresa = skladData.adresa;
    skladiste.broj_skladisnih_jedinica = skladData.kapacitet;

    if(skladData.adresa == null || skladData.kapacitet==null)
    {
      this.toastr.warning("Niste unijeli sve podatke", 'UPOZORENJE');
      return;
    }

    this.userService.addStorage(skladiste).subscribe(data =>
      {
        this.toastr.success("Uspješno dodano skladište", 'USPJEH');
      }, error => 
      {
        console.log(error);
        this.toastr.error("Skladište nije dodano", 'GREŠKA');
      });
    this.dodavanjeSklad = false;
    this.forma.reset();
  }

  dodajSkladJed()
  {
    this.dodavanjeSJed = true;
    this.dodavanjeSklad = false;
    this.userService.findAllStorages().subscribe(data => 
      {
        this.svaSkladista = data;
        console.log(this.svaSkladista);
      }, 
      error => 
      {
        console.log(error);
        this.toastr.error("Greška pri učitavanju", 'GREŠKA');
      });

    this.userService.findAllTypes().subscribe(data => 
        {
          this.sviTipovi = data;
          console.log(this.sviTipovi);
        }, 
        error => 
        {
          console.log(error);
          this.toastr.error("Greška pri učitavanju", 'GREŠKA');
        });
  }

  postSkladJed(skladData)
  {
    var skladiste = skladData.skladChoose;
    var tip = skladData.tipChoose;

    if(skladiste == null || tip==null || skladData.inputBrojJed==null)
    {
      this.toastr.warning("Niste unijeli sve podatke", 'UPOZORENJE');
      return;
    }
    console.log(skladiste);

    this.userService.addStorageUnit(tip,skladiste,skladData.inputBrojJed).subscribe(data =>
      {
        this.toastr.success("Uspješno dodana jedinica", 'USPJEH');
      }, error => 
      {
        console.log(error);
        this.toastr.error("Jedinica nije dodana", 'GREŠKA');
      });
    this.dodavanjeSJed = false;
    this.forma2.reset();
  }

  obrisiSkladiste()
  {

  }

  obrisiSkladJed()
  {

  }


  changeCity(e) {
  }

}
