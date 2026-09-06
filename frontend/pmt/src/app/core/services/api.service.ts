// src/app/core/services/api.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { Project } from '../models/project.model';
import { Task } from '../models/task.model';

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface UserProject {
  id: { userId: number; projectId: number };
  role: 'ADMINISTRATEUR' | 'MEMBRE' | 'OBSERVATEUR';
}

export interface TaskHistory {
  id?: number;
  name: string;
  description?: string;
  dueDate?: string | Date;
  endDate?: string | Date;
  priority?: number;
  assignedTo?: string;
  status: 'TODO' | 'IN_PROGRESS' | 'DONE';
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) {}

  // --- USERS[cite: 6] ---
  register(user: User): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/users`, user);
  }

  login(credentials: LoginCredentials): Observable<User> {
    return this.http.post<User>(`${this.baseUrl}/users/login`, credentials);
  }

  // --- PROJECTS[cite: 2] ---
  getProject(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.baseUrl}/projects/${id}`);
  }

  createProject(userId: number, project: Project): Observable<Project> {
    return this.http.post<Project>(`${this.baseUrl}/projects/${userId}`, project);
  }

  getProjects(): Observable<Project[]> {
    return this.http.get<Project[]>(`${this.baseUrl}/projects`);
  }

  getProjectMembers(projectId: number): Observable<UserProject[]> {
    return this.http.get<UserProject[]>(`${this.baseUrl}/users-projects/projects/${projectId}`);
  }

  inviteMember(projectId: number, email: string, role: UserProject['role'] = 'MEMBRE'): Observable<UserProject> {
    return this.http.post<UserProject>(`${this.baseUrl}/users-projects?email=${encodeURIComponent(email)}`, {
      id: { userId: 0, projectId },
      role
    });
  }

  updateMemberRole(userId: number, projectId: number, role: UserProject['role']): Observable<UserProject> {
    return this.http.put<UserProject>(`${this.baseUrl}/users-projects/users/${userId}/projects/${projectId}`, {
      role
    });
  }

  // --- TASKS[cite: 4] ---
  getTasksByProject(projectId: number): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.baseUrl}/tasks/projects/${projectId}`);
  }

  createTask(projectId: number, task: Task): Observable<Task> {
    return this.http.post<Task>(`${this.baseUrl}/tasks/projects/${projectId}`, task);
  }

  updateTask(taskId: number, task: Task): Observable<Task> {
    return this.http.put<Task>(`${this.baseUrl}/tasks/${taskId}`, task);
  }

  getTasksByStatus(projectId: number, status: Task['status']): Observable<Task[]> {
    return this.http.get<Task[]>(`${this.baseUrl}/tasks/projects/${projectId}/status/${status}`);
  }

  getTaskHistory(taskId: number): Observable<TaskHistory[]> {
    return this.http.get<TaskHistory[]>(`${this.baseUrl}/tasks-history/tasks/${taskId}`);
  }
}
