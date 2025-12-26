import { Routes } from '@angular/router';
import { Dashboard } from './components/dashboard/dashboard';
import { InterviewRoom } from './components/interview-room/interview-room';
import { Login } from './components/login/login';
import { Pofile } from './components/pofile/pofile';
import { HistoryComponent } from './components/history/history';
import { authGuard } from './guards/auth-guard';
import { Signup } from './components/signup/signup';
import { Feedback } from './components/feedback/feedback';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'signup', component: Signup },
  { path: 'dashboard', component: Dashboard },
  { path: 'interview/:category', component: InterviewRoom ,canActivate:[authGuard]},
  { path: 'feedback/:id', component: Feedback ,canActivate:[authGuard]},
  { path: 'history', component: HistoryComponent ,canActivate:[authGuard]},
  { path: 'profile', component: Pofile ,canActivate:[authGuard]}
]
