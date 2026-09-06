import { TestBed } from '@angular/core/testing';
import { provideRouter, Router } from '@angular/router';
import { HomeComponent } from './home.component';

describe('HomeComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeComponent],
      providers: [provideRouter([])]
    }).compileComponents();
  });

  it('devrait créer le composant et afficher le message d accueil', async () => {
    const fixture = TestBed.createComponent(HomeComponent);
    await fixture.whenStable();

    expect(fixture.componentInstance).toBeTruthy();
    expect(fixture.nativeElement.textContent).toContain('Bienvenue sur PMT');
  });

  it('devrait exposer les liens de connexion et d espace de travail', async () => {
    const fixture = TestBed.createComponent(HomeComponent);
    await fixture.whenStable();
    const buttons = Array.from(fixture.nativeElement.querySelectorAll('button')) as HTMLButtonElement[];

    expect(buttons.map(button => button.getAttribute('routerLink'))).toEqual(['/auth', '/workspace']);
  });
});
