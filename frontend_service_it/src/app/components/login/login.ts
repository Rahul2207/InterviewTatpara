import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { Auth } from '../../services/auth';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  imports: [FormsModule,CommonModule,RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
creds = { email: '', password: '' };
  loading = false;
  error = '';

  constructor(private authService: Auth, private router: Router) {}

  onLogin() {
    this.loading = true;
    this.error = '';
    
    this.authService.login(this.creds).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        // Show a cleaner error message
        this.error = 'Invalid email or password.';
        this.loading = false;
      }
    });
  }
}
