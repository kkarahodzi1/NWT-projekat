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
  config;

  constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService) {
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
    this.userService.login(this.user).subscribe(data => {
      window.sessionStorage.setItem('token', JSON.stringify(data));
      console.log(window.sessionStorage.getItem('token'));
      this.router.navigate(['userview']);
    }, error => {
        alert("Neispravni podaci")
    });
    console.log(this.user.password);
  }
}
