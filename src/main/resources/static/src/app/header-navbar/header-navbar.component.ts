import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'mtips-header-navbar',
  templateUrl: './header-navbar.component.html',
  styleUrls: ['./header-navbar.component.scss']
})
export class HeaderNavbarComponent {
  constructor(private router: Router) {}

}
