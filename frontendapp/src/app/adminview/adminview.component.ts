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
import { SkladisnaJedinica } from '../models/skladisnaJedinica';
// import {MatMenuModule} from '@angular/material/menu';
// ne da mi da ovo importujem, ali se svakako ne koristi

@Component({
  selector: 'app-adminview',
  templateUrl: './adminview.component.html',
  styleUrls: ['./adminview.component.css']
})
export class AdminviewComponent implements OnInit {
  korisnik: User;
  dodavanjeSklad: boolean;
  dodavanjeSJed: boolean;
  brisanjeSklad: boolean;
  brisanjeSJed: boolean;
  forma;
  forma2;
  forma3;
  forma4;
  svaSkladista: Skladiste[];
  sviTipovi: Tip[];
  sveJedinice: SkladisnaJedinica[];

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
    this.forma3 = this.formBuilder.group({
      skladChoose: ['']
    });
    this.forma4 = this.formBuilder.group({
      skladChoose: [''],
      sjedChoose: ['']
    });
    this.svaSkladista = new Array<Skladiste>();
    this.sviTipovi = new Array<Tip>();
    this.sveJedinice = new Array<SkladisnaJedinica>();
  }

  ngOnInit() {
    this.dodavanjeSklad = false;
    this.dodavanjeSJed = false;
    this.brisanjeSJed = false;
    this.brisanjeSklad = false;
    if (window.sessionStorage.getItem('token') == null) {
      this.toastr.error('Korisnik nije prijavljen!', 'GREŠKA');
      this.router.navigate(['']);
    } else if (JSON.parse(window.sessionStorage.getItem('token')).client.role === 0) {
      this.router.navigate(['userview']);
    }
    this.korisnik = JSON.parse(window.sessionStorage.getItem('token')).client;
  }

  dodajSkladiste() {
    this.dodavanjeSJed = false;
    this.dodavanjeSklad = true;
    this.brisanjeSJed = false;
    this.brisanjeSklad = false;
  }

  postSkladiste(skladData) {
    const skladiste = new Skladiste;
    skladiste.adresa = skladData.adresa;
    skladiste.broj_skladisnih_jedinica = skladData.kapacitet;

    if (skladData.adresa == "" || skladData.kapacitet == "") {
      this.toastr.warning('Niste unijeli sve podatke', 'UPOZORENJE');
      return;
    }

    this.userService.addStorage(skladiste).subscribe(data => {
        this.toastr.success('Uspješno dodano skladište', 'USPJEH');
      }, error => {
        console.log(error);
        this.toastr.error('Skladište nije dodano', 'GREŠKA');
      });
    this.dodavanjeSklad = false;
    this.forma.reset();
  }

  dodajSkladJed() {
    this.dodavanjeSJed = true;
    this.dodavanjeSklad = false;
    this.brisanjeSJed = false;
    this.brisanjeSklad = false;
    this.userService.findAllStorages().subscribe(data => {
        this.svaSkladista = data;
        console.log(this.svaSkladista);
      },
      error => {
        console.log(error);
        this.toastr.error('Greška pri učitavanju', 'GREŠKA');
      });

    this.userService.findAllTypes().subscribe(data => {
          this.sviTipovi = data;
          console.log(this.sviTipovi);
        },
        error => {
          console.log(error);
          this.toastr.error('Greška pri učitavanju', 'GREŠKA');
        });
  }

  postSkladJed(skladData) {
    const skladiste = skladData.skladChoose;
    const tip = skladData.tipChoose;

    if (skladiste == null || tip == null ||  skladiste == "" || tip == "" || skladData.inputBrojJed == null) {
      this.toastr.warning('Niste unijeli sve podatke', 'UPOZORENJE');
      return;
    }
    console.log(skladiste);

    this.userService.addStorageUnit(tip, skladiste, skladData.inputBrojJed).subscribe(data => {
        this.toastr.success('Uspješno dodana jedinica', 'USPJEH');
      }, error => {
        console.log(error);
        this.toastr.error('Jedinica nije dodana', 'GREŠKA');
      });
    this.dodavanjeSJed = false;
    this.forma2.reset();
  }

  obrisiSkladiste() {
    this.dodavanjeSJed = false;
    this.dodavanjeSklad = false;
    this.brisanjeSJed = false;
    this.brisanjeSklad = true;

    this.userService.findAllStorages().subscribe(data => {
      this.svaSkladista = data;
      console.log(this.svaSkladista);
    },
    error => {
      console.log(error);
      this.toastr.error('Greška pri učitavanju', 'GREŠKA');
    });
  }

  deleteSklad(skladData)
  {
    console.log(skladData.skladChoose)
    if (skladData.skladChoose == "" || skladData.skladChoose == null) {
      this.toastr.warning('Niste unijeli sve podatke', 'UPOZORENJE');
      return;
    }
    this.userService.deleteStorage(skladData.skladChoose).subscribe(data => {
      console.log(data);
      this.toastr.success("Skladište je uspješno obrisano", "USPJEH");
    },
    error => {
      console.log(error.status);
      if(error.status == 417)
        this.toastr.error('Nije moguće obrisati skladište sa aktivnim jedinicma', 'GREŠKA');
      else
        this.toastr.error('Greška pri brisanju', 'GREŠKA');
    });

    this.brisanjeSklad = false;
    this.forma3.reset();
  }

  obrisiSkladJed() {
    this.dodavanjeSJed = false;
    this.dodavanjeSklad = false;
    this.brisanjeSJed = true;
    this.brisanjeSklad = false;

    this.userService.findAllStorages().subscribe(data => {
      this.svaSkladista = data;
      console.log(this.svaSkladista);
    },
    error => {
      console.log(error);
      this.toastr.error('Greška pri učitavanju', 'GREŠKA');
    });
  }

  onChange(skladData)
  {
    console.log(skladData.skladChoose)
    this.userService.findAllUnits(skladData.skladChoose).subscribe(data=>
      {
        console.log("Radim")
        console.log(data)
        this.sveJedinice = data;
      },
      error => {
        console.log(error);
        this.toastr.error('Greška pri učitavanju', 'GREŠKA');
      });
  }

  deleteSJed(sjedData)
  {
    console.log(sjedData.sjedChoose)
    if (sjedData.sjedChoose == "" || sjedData.sjedChoose == null) {
      this.toastr.warning('Niste unijeli sve podatke', 'UPOZORENJE');
      return;
    }
    this.userService.deleteUnit(sjedData.sjedChoose).subscribe(data => {
      console.log(data);
      this.toastr.success("Skladišna jedinica je uspješno obrisana", "USPJEH");
    },
    error => {
      console.log(error);
      
        this.toastr.error('Greška pri brisanju', 'GREŠKA');
    });

    this.brisanjeSJed = false;
    this.forma4.reset();
  }
}
