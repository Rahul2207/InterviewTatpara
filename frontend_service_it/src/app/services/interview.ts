import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Interview {
  private baseUrl = 'http://localhost:9091/api';

  constructor(private http: HttpClient) { }

  getQuestionsByCategory(category: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/questions/${category}`);
  }

  uploadVideo(file: File, email: string, question: string, category: string): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('email', email);
    formData.append('question', question);
    formData.append('category', category);

    return this.http.post(`${this.baseUrl}/interview/upload`, formData);
  }
  private apiUrl = 'http://localhost:9091/api/interview'; 
private analysisUrl  = 'http://localhost:9092/api/analysis';
  

  // saveResult(result: any) {
  //   return this.http.post(this.apiUrl, result);
  // }

  getHistory(email: string) {
    return this.http.get<any[]>(`${this.apiUrl}/history/${email}`);
  }

  getSession(sessionId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${sessionId}`);
  }
  generateQuestions(category: string): Observable<any> {
    return this.http.post(`${this.analysisUrl}/generate-questions`, { category });
  }
}
