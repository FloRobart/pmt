// src/app/app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './modules/home/home.component';
import { AuthComponent } from './modules/auth/auth.component';
import { WorkspaceComponent } from './modules/workspace/workspace.component';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Page d'accueil par défaut
  { path: 'auth', component: AuthComponent }, // Page de connexion/inscription
  { path: 'workspace', component: WorkspaceComponent }, // Espace projet
  { path: '**', redirectTo: '' } // Redirection des routes inconnues vers l'accueil
];
