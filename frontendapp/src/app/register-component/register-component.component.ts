import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { User } from '../models/user';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-register-component',
  templateUrl: './register-component.component.html',
  styleUrls: ['./register-component.component.css']
})
export class RegisterComponentComponent implements OnInit {
  loginForm;
  user: User;
  config;

  constructor(private formBuilder: FormBuilder, private router: Router, private userService: UserService) {
    this.loginForm = this.formBuilder.group({
      name: '',
      lname: '',
      email: '',
      password: ''
    });
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
    this.userService.save(this.user).subscribe(response => console.log(response), err => console.log(err));
  }

}
