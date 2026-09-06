import { Component } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { User } from '../../core/models/user.model';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  imports: [FormsModule]
})
export class AuthComponent {
  isLoginMode = true;
  // Initialisation propre pour éviter les erreurs TS strictes
  user: User = { email: '', password: '', username: '' };
  loggedInUser: User | null = null;

  constructor(private api: ApiService, private router: Router) {}

  toggleMode() {
    this.isLoginMode = !this.isLoginMode;
  }

  onSubmit() {
    if (this.isLoginMode) {
      this.api.login({ email: this.user.email, password: this.user.password ?? '' }).subscribe(res => {
        this.loggedInUser = res;
        if (res.id) {
          localStorage.setItem('userId', res.id.toString());
          this.router.navigate(['/workspace']);
        }
      });
    } else {
      this.api.register(this.user).subscribe(() => this.isLoginMode = true);
    }
  }
}
