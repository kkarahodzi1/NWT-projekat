import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { Zakupnina } from '../models/zakupnina';
import { Skladiste } from '../models/skladiste';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { promise } from 'protractor';
import { FormBuilder } from '@angular/forms';


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


  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router, private toastr: ToastrService) {
    this.forma = this.formBuilder.group({
      adresa: '',
      kapacitet: ''
    });
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

  }

  obrisiSkladiste()
  {

  }

  obrisiSkladJed()
  {

  }

}
