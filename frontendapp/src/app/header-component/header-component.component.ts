import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';

@Component({
  selector: 'app-header-component',
  templateUrl: './header-component.component.html',
  styleUrls: ['./header-component.component.css']
})
export class HeaderComponentComponent implements OnInit {
  loggedin: boolean;
  constructor(public router: Router) {
    router.events.subscribe( (event) => ( event instanceof NavigationEnd ) && this.handleRouteChange());
    this.loggedin = false;
   }

  ngOnInit() {
    this.handleRouteChange();
  }
  handleRouteChange = () => {
    if (window.sessionStorage.getItem('token') == null) {
        this.loggedin = false;
    } else {
      this.loggedin = true;
    }
  }

  odjavi() {
    window.sessionStorage.clear();
  }

}
