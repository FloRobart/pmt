import { TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { of } from 'rxjs';
import { ApiService } from '../../core/services/api.service';
import { AuthComponent } from './auth.component';

describe('AuthComponent', () => {
  const api = {
    login: vi.fn(),
    register: vi.fn()
  };

  beforeEach(async () => {
    vi.clearAllMocks();
    localStorage.clear();
    await TestBed.configureTestingModule({
      imports: [AuthComponent],
      providers: [
        { provide: ApiService, useValue: api },
        provideRouter([])
      ]
    }).compileComponents();
  });

  it('devrait initialiser le formulaire en mode connexion', () => {
    const component = TestBed.createComponent(AuthComponent).componentInstance;

    expect(component.isLoginMode).toBe(true);
    expect(component.user).toEqual({ email: '', password: '', username: '' });
  });

  it('devrait basculer entre connexion et inscription', () => {
    const component = TestBed.createComponent(AuthComponent).componentInstance;

    component.toggleMode();
    expect(component.isLoginMode).toBe(false);
    component.toggleMode();
    expect(component.isLoginMode).toBe(true);
  });

  it('devrait connecter l utilisateur et naviguer vers l espace', () => {
    const component = TestBed.createComponent(AuthComponent).componentInstance;
    const router = TestBed.inject(Router);
    const navigateSpy = vi.spyOn(router, 'navigate').mockResolvedValue(true);
    component.user = { email: 'user@example.com', password: 'secret' };
    api.login.mockReturnValue(of({ id: 7, email: 'user@example.com', username: 'User' }));

    component.onSubmit();

    expect(api.login).toHaveBeenCalledWith({ email: 'user@example.com', password: 'secret' });
    expect(component.loggedInUser?.id).toBe(7);
    expect(localStorage.getItem('userId')).toBe('7');
    expect(navigateSpy).toHaveBeenCalledWith(['/workspace']);
  });

  it('devrait revenir au mode connexion après une inscription', () => {
    const component = TestBed.createComponent(AuthComponent).componentInstance;
    component.isLoginMode = false;
    component.user = { username: 'New user', email: 'new@example.com', password: 'secret' };
    api.register.mockReturnValue(of(component.user));

    component.onSubmit();

    expect(api.register).toHaveBeenCalledWith(component.user);
    expect(component.isLoginMode).toBe(true);
  });

  it('devrait afficher le formulaire de connexion', () => {
    const fixture = TestBed.createComponent(AuthComponent);

    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('h2').textContent).toContain('Connexion');
    expect(fixture.nativeElement.querySelector('input[name="email"]')).not.toBeNull();
    expect(fixture.nativeElement.querySelector('input[name="username"]')).toBeNull();
    expect(fixture.nativeElement.querySelector('button').textContent).toContain('Se connecter');
  });

  it('devrait afficher le formulaire d inscription', () => {
    const fixture = TestBed.createComponent(AuthComponent);
    fixture.componentInstance.isLoginMode = false;

    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('h2').textContent).toContain('Inscription');
    expect(fixture.nativeElement.querySelector('input[name="username"]')).not.toBeNull();
    expect(fixture.nativeElement.querySelector('button').textContent).toContain("S'inscrire");
  });

  it('devrait afficher le message de bienvenue après connexion', () => {
    const fixture = TestBed.createComponent(AuthComponent);
    fixture.componentInstance.loggedInUser = { id: 7, username: 'User', email: 'user@example.com' };

    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('.success-message').textContent)
      .toContain('Bienvenue User');
    expect(fixture.nativeElement.querySelector('form')).toBeNull();
  });

  it('devrait conserver le mode connexion si la réponse ne contient pas d identifiant', () => {
    const component = TestBed.createComponent(AuthComponent).componentInstance;
    const router = TestBed.inject(Router);
    const navigateSpy = vi.spyOn(router, 'navigate');
    api.login.mockReturnValue(of({ email: 'user@example.com', username: 'User' }));

    component.onSubmit();

    expect(component.loggedInUser?.email).toBe('user@example.com');
    expect(localStorage.getItem('userId')).toBeNull();
    expect(navigateSpy).not.toHaveBeenCalled();
  });
});
