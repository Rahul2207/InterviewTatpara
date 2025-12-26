import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface Track {
  id: string;
  title: string;
  icon: string;
  desc: string;
  colorBg: string;
}
@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css',
})
export class Dashboard {
tracks: Track[] = [
    { 
      id: 'JAVA', 
      title: 'Java Expert', 
      icon: '☕', 
      desc: 'Core Java, Spring Boot, & System Design.', 
      colorBg: 'bg-soft-orange' 
    },
    { 
      id: 'HR', 
      title: 'HR Round', 
      icon: '🤝', 
      desc: 'Behavioral fit, strengths, and weaknesses.', 
      colorBg: 'bg-soft-green' 
    },
    { 
      id: 'SQL', 
      title: 'Database', 
      icon: '💾', 
      desc: 'Complex queries, normalization, and ACID.', 
      colorBg: 'bg-soft-blue' 
    },
    { 
      id: 'CUSTOM', 
      title: 'Custom', 
      icon: '✨', 
      desc: 'Create your own specific scenario.', 
      colorBg: 'bg-soft-purple' 
    }
  ];

  constructor(private router: Router) {}

  go(id: string) { 
    this.router.navigate(['/interview', id]); 
  }
}
