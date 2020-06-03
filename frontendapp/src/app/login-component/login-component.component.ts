import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { FormsModule, ReactiveFormsModule, NG_VALIDATORS, Validator, AbstractControl, ValidatorFn } from '@angular/forms';
import { FormGroup, FormControl, Validators, Form, NgForm} from '@angular/forms';

@Component({
  selector: 'app-login-component',
  templateUrl: './login-component.component.html',
  styleUrls: ['./login-component.component.css']
})
export class LoginComponentComponent implements OnInit {
  loginForm;
  user: User;
  config;
  fm: FormGroup;
  public unos = {email: null, password: null};
  public validmail: RegExp;

  constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService, private toastr: ToastrService) {
    this.loginForm = this.formBuilder.group({
      email: '',
      password: ''
    });
    this.validmail = new RegExp('^([\\w\\-\\.]+)@((\\[([0-9]{1,3}\\.){3}[0-9]{1,3}\\])|(([\\w\\-]+\\.)+)([a-zA-Z]{2,4}))$');
    this.user = new User();
     }

  ngOnInit() {
    const t = window.sessionStorage.getItem('token');
    if (t != null) {
      this.router.navigate(['userview']);
    } else {
      window.sessionStorage.clear();
    }

  }

  login(data) {

    this.user.mail = data.email;
    this.user.password = data.password;
    this.userService.login(this.user).subscribe(data => {
      window.sessionStorage.setItem('token', JSON.stringify(data));
      this.router.navigate(['userview']);
    }, error => {
      this.toastr.error('Neispravni podaci","Neuspješan login');
    });

  }
}
