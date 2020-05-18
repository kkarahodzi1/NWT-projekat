import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-login-component',
  templateUrl: './login-component.component.html',
  styleUrls: ['./login-component.component.css']
})
export class LoginComponentComponent implements OnInit {
  loginForm;
  user: User;
  constructor(private formBuilder: FormBuilder, private userService: UserService) {
    this.loginForm = this.formBuilder.group({
      email: '',
      password: ''
    });

    this.user = new User();
     }

  ngOnInit() {
  }

  login(data){
    this.user.mail = data.email;
    this.user.password = data.password;
    this.userService.login(this.user);
    console.log(this.user.mail);
    console.log(this.user.password);
  }
}
