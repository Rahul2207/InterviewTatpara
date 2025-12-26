import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Interview } from '../../services/interview';

@Component({
  selector: 'app-feedback',
  imports: [CommonModule,RouterLink],
  templateUrl: './feedback.html',
  styleUrl: './feedback.css',
})
export class Feedback implements OnInit,OnDestroy{
sessionId: string | null = null;
  interviewData: any = null;
  isLoading = true;
  pollInterval: any;

  constructor(
    private route: ActivatedRoute,
    private interviewService: Interview
  ) {}

  ngOnInit() {
    // 1. Get the Session ID from the URL (e.g., /feedback/123-abc)
    this.sessionId = this.route.snapshot.paramMap.get('id');

    if (this.sessionId) {
      this.startPolling();
    }
  }

  startPolling() {
    this.pollInterval = setInterval(() => {
      this.checkStatus();
    }, 5000); // Check every 5 seconds
    
    // Check immediately once on load
    this.checkStatus();
  }

  checkStatus() {
    if (!this.sessionId) return;

    this.interviewService.getSession(this.sessionId).subscribe({
      next: (data) => {
        if (data.status === 'COMPLETED') {
          this.interviewData = data;
          this.isLoading = false;
          this.stopPolling(); // Stop checking once we have results
        } else if (data.status === 'FAILED') {
          this.isLoading = false;
          this.stopPolling();
          alert('Analysis Failed. Please try again.');
        }
        // If status is 'PROCESSING' or 'UPLOADING', do nothing and wait for next interval
      },
      error: (err) => console.error('Error polling status:', err)
    });
  }

  stopPolling() {
    if (this.pollInterval) {
      clearInterval(this.pollInterval);
    }
  }

  ngOnDestroy() {
    this.stopPolling(); // Cleanup when leaving the page
  }
}
