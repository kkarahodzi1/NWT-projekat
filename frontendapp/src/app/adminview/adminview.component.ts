import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-adminview',
  templateUrl: './adminview.component.html',
  styleUrls: ['./adminview.component.css']
})
export class AdminviewComponent implements OnInit {
  korisnik: User;
  constructor(private userService: UserService, private router: Router, private toastr: ToastrService) {}


  ngOnInit() {
    if (window.sessionStorage.getItem('token') == null) {
      this.toastr.error('Korisnik nije prijavljen!', 'GREŠKA');
      this.router.navigate(['']);
    } else if (JSON.parse(window.sessionStorage.getItem('token')).client.role === 0) {
      this.router.navigate(['userview']);
    }
  }

}
