import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register-component',
  templateUrl: './register-component.component.html',
  styleUrls: ['./register-component.component.css']
})
export class RegisterComponentComponent implements OnInit {
  loginForm;
  user: User;
  config;
  public validmail: RegExp;

  constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService, private toastr: ToastrService) {
    this.loginForm = this.formBuilder.group({
      name: '',
      lname: '',
      email: '',
      password: ''
    });
    this.validmail = new RegExp('^([\\w\\-\\.]+)@((\\[([0-9]{1,3}\\.){3}[0-9]{1,3}\\])|(([\\w\\-]+\\.)+)([a-zA-Z]{2,4}))$');
    this.user = new User();
   }

  ngOnInit() {
    window.sessionStorage.clear();
  }


  register(data){
    this.user.mail = data.email;
    this.user.password = data.password;
    this.user.ime = data.name;
    this.user.prezime = data.lname;
    this.userService.save(this.user).subscribe(response => {
        this.toastr.success('Registracija uspjesna!');
        this.router.navigate(['']);
      }, err => {
        const r = err;
        this.toastr.error('GRESKA: ' + err.error.errmsg);
        console.log(err);
      });
  }

}
