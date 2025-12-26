import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../services/auth';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-signup',
  imports: [FormsModule,CommonModule,RouterLink],
  templateUrl: './signup.html',
  styleUrl: './signup.css',
})
export class Signup {
user = { name: '', email: '', password: '' };
  loading = false;
  error = '';

  constructor(private authService: Auth, private router: Router) {}

  onSignup() {
    this.loading = true;
    this.error = '';

    console.log(this.user);
    this.authService.signup(this.user).subscribe({
      next: () => {
        // Success! Redirect to Login
        this.router.navigate(['/login']);
        alert('Account created! Please log in.');
      },
      error: (err) => {
        console.error(err);
        this.error = 'Signup failed. Email might already exist.';
        this.loading = false;
      }
    });
  }
}
