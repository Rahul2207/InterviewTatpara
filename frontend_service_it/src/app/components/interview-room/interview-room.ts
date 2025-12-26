import { Component, ElementRef, ViewChild, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Auth } from '../../services/auth';
import { Interview } from '../../services/interview';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

interface QuestionItem {
  text: string;
  status: 'pending' | 'active' | 'completed';
}

@Component({
  selector: 'app-interview-room',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './interview-room.html', // Make sure this matches your file name
  styleUrl: './interview-room.css',
})
export class InterviewRoom implements OnInit, OnDestroy {
  
  // --- STATE ---
  questionsList: QuestionItem[] = []; 
  currentQuestionIndex: number = -1;
  
  // User & Data
  email = '';
  category = 'General'; // Default category
  question = ''; 
  
  // UI State
  showAiGen = false;
  isGenerating = false;
  mode: 'upload' | 'record' = 'upload';
  loading = false;
  message = '';
  isError = false;

  // Media vars
  file: File | null = null;
  @ViewChild('videoPreview') videoEl!: ElementRef<HTMLVideoElement>;
  mediaRecorder: MediaRecorder | null = null;
  chunks: Blob[] = [];
  isRecording = false;
  stream: MediaStream | null = null;
  recordedUrl: string | null = null;

  constructor(
    private interview: Interview,
    private auth: Auth,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.email = this.auth.getUserEmail();
    
    // If URL has a category (e.g. /interview/Java), use it
    const routeCategory = this.route.snapshot.paramMap.get('category');
    if (routeCategory && routeCategory !== 'CUSTOM') {
      this.category = routeCategory;
      this.generateAiQuestions(); // Auto-generate if category exists
    }
  }

  // --- GENERATE LIST ---
  generateAiQuestions() {
    if (!this.category.trim()) {
      this.message = "Please enter a category first.";
      this.isError = true;
      return;
    }

    this.isGenerating = true;
    this.questionsList = []; 

    // Use 'this.category' to ask AI for questions
    this.interview.generateQuestions(this.category).subscribe({
      next: (res: any) => {
        const questions: string[] = res.questions || [res.question]; 
        
        this.questionsList = questions.map(q => ({
          text: q,
          status: 'pending'
        }));

        if (this.questionsList.length > 0) {
          this.selectQuestion(0);
        }

        this.isGenerating = false;
      },
      error: (err) => {
        this.isGenerating = false;
        this.message = 'Failed to generate questions';
        this.isError = true;
      }
    });
  }

  selectQuestion(index: number) {
    this.currentQuestionIndex = index;
    this.question = this.questionsList[index].text;
    
    this.questionsList.forEach(q => {
      if (q.status === 'active') q.status = 'pending';
    });
    
    if (this.questionsList[index].status !== 'completed') {
      this.questionsList[index].status = 'active';
    }
  }

  // --- CAMERA & MEDIA LOGIC ---
  setMode(m: 'upload' | 'record') { this.mode = m; this.stopCamera(); this.file = null; this.recordedUrl = null; }
  
  async startCamera() { 
    try {
      this.stream = await navigator.mediaDevices.getUserMedia({ video: true, audio: true });
      this.videoEl.nativeElement.srcObject = this.stream;
    } catch(e) { console.error(e); }
  }
  
  stopCamera() { this.stream?.getTracks().forEach(t => t.stop()); }
  
  startRec() { 
    if(!this.stream) return;
    this.chunks = [];
    this.mediaRecorder = new MediaRecorder(this.stream);
    this.mediaRecorder.ondataavailable = e => { if(e.data.size > 0) this.chunks.push(e.data); };
    this.mediaRecorder.onstop = () => {
      const blob = new Blob(this.chunks, { type: 'video/webm' });
      this.recordedUrl = URL.createObjectURL(blob);
      this.file = new File([blob], "recorded.webm", { type: 'video/webm' });
      this.stopCamera();
    };
    this.mediaRecorder.start();
    this.isRecording = true;
  }
  
  stopRec() { this.mediaRecorder?.stop(); this.isRecording = false; }
  onFile(e: any) { this.file = e.target.files[0]; }
  ngOnDestroy() { this.stopCamera(); }

  // --- SUBMIT ---
  submit() {
    if (!this.file || !this.question || !this.category) {
      this.message = "Missing file, question, or category.";
      this.isError = true;
      return;
    }

    this.loading = true;
    this.message = 'Uploading & Analyzing...';
    
    // SENDING 4 THINGS: File, Email, Question, Category
    this.interview.uploadVideo(this.file, this.email, this.question, this.category)
      .subscribe({
        next: (res: any) => {
          this.loading = false;
          console.log(res);
          if (this.currentQuestionIndex > -1) {
            this.questionsList[this.currentQuestionIndex].status = 'completed';
          }
              
   this.router.navigate(['/feedback', res.id]); // res.id now exists!
        },
        error: (err) => {
          this.loading = false;
          this.message = 'Error: ' + err.message;
          this.isError = true;
        }
      });
  }
}