import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-userview',
  templateUrl: './userview.component.html',
  styleUrls: ['./userview.component.css']
})
export class UserviewComponent implements OnInit {

  constructor(private userService: UserService) {}

  ngOnInit() {
    this.userService.findAll().subscribe(data => {
      console.log(data);
    }, error => {
      console.log(error);
    });
  }

}
