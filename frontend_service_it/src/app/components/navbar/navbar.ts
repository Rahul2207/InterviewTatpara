import { Component, HostListener, inject } from '@angular/core';
import { Auth } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-navbar',
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
isLoggedIn = false;
  userEmail: string | null = '';

  constructor(public auth: Auth, private router: Router) {}

  ngOnInit() {
    // Subscribe to auth changes if your service supports it, 
    // or just check on load:
    this.checkLoginStatus();
    
    // Optional: Refresh status on route change if Auth service is static
    this.router.events.subscribe(() => this.checkLoginStatus());
  }

  checkLoginStatus() {
    this.isLoggedIn = this.auth.isAuthenticated();
    if (this.isLoggedIn) {
      this.userEmail = this.auth.getUserEmail();
    }
  }

  logout() {
    this.auth.logout();
    this.isLoggedIn = false;
    this.router.navigate(['/login']);
  }
  // Update closeMenu to close BOTH when a link is clicked
  
}
