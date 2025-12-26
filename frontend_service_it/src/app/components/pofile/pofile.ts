import { Component } from '@angular/core';
import { Auth } from '../../services/auth';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pofile',
  imports: [CommonModule,FormsModule],
  templateUrl: './pofile.html',
  styleUrl: './pofile.css',
})
export class Pofile {
// Mock Data (In real app, fetch from backend)
  user = {
    name: 'Rahul Kumar',
    email: '',
    phone: '9373198749',
    role: 'Java Developer',
    targetRole: 'Senior Backend Engineer',
    bio: 'Passionate about building scalable microservices and mastering system design.',
    location: 'Patna, Bihar'
  };

  stats = {
    interviewsTaken: 12,
    avgScore: 7.5,
    highestScore: 9
  };

  isEditing = false;
  isLoading = false;

  constructor(private auth: Auth) {}

  ngOnInit() {
    this.user.email = this.auth.getUserEmail();
  }

  toggleEdit() {
    this.isEditing = !this.isEditing;
  }

  saveProfile() {
    this.isLoading = true;
    // Simulate API call
    setTimeout(() => {
      this.isLoading = false;
      this.isEditing = false;
      // You would call a service to update DB here
      alert('Profile updated successfully!');
    }, 1500);
  }
}
