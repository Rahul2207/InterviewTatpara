import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private authUrl = 'http://localhost:9090/api/auth'; // Check your Java Controller path!

  private currentUserSubject = new BehaviorSubject<any>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    const savedUser = localStorage.getItem('currentUser');
    if (savedUser) {
      this.currentUserSubject.next(JSON.parse(savedUser));
    }
  }
isAuthenticated(): boolean {
    const token = localStorage.getItem('token'); 
    // The '!!' converts the string/null to a boolean (true/false)
    return !!token; 
  }
  signup(user: any): Observable<any> {
  // Calls POST http://localhost:8083/api/auth/signup
  return this.http.post(`${this.authUrl}/register`, user,{responseType:"text"});
}

  login(credentials: any): Observable<any> {
  return this.http.post(`${this.authUrl}/login`, credentials).pipe(
    tap((response: any) => {
      // The backend now returns { name, email, token }
      localStorage.setItem('currentUser', JSON.stringify(response));
      // Also save the token specifically if you want
      localStorage.setItem('token', response.token); 
      
      this.currentUserSubject.next(response);
    })
  );
}

  logout() {
    localStorage.clear();
    localStorage.removeItem('currentUser');
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!this.currentUserSubject.value;
  }

  getUserEmail(): string {
    return this.currentUserSubject.value?.email || '';
  }
}
