import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Auth } from '../../services/auth';
import { Interview } from '../../services/interview';

@Component({
  selector: 'app-history',
  imports: [CommonModule,RouterLink],
  templateUrl: './history.html',
  styleUrl: './history.css',
})
export class HistoryComponent implements OnInit{
historyList: any[] = [];
  isLoading = true;

  constructor(
    private interviewService: Interview,
    private authService: Auth
  ) {}

  ngOnInit() {
    // Subscribe to the Observable to get the User object
    this.authService.currentUser$.subscribe(user => {
      
      // Check if user exists and has an email
      if (user && user.email) {
        this.fetchHistory(user.email);
      } else {
        // Handle case where user isn't logged in properly
        this.isLoading = false; 
      }
      
    });
  }

  fetchHistory(email: string) {
    this.interviewService.getHistory(email).subscribe({
      next: (data) => {
        this.historyList = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Failed to load history', err);
        this.isLoading = false;
      }
    });
  }
}
