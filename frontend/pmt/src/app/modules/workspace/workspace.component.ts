import { Component } from '@angular/core';
import { ApiService } from '../../core/services/api.service';
import { Project } from '../../core/models/project.model';
import { Task } from '../../core/models/task.model';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { UserProject, TaskHistory } from '../../core/services/api.service';

@Component({
  selector: 'app-workspace',
  templateUrl: './workspace.component.html',
  imports: [FormsModule, CommonModule]
})
export class WorkspaceComponent {
  project: Project = { name: '', description: '', startDate: new Date().toISOString().split('T')[0] };
  currentProject: Project | null = null;

  tasks: Task[] = [];
  newTask: Task = this.getEmptyTask(0);
  members: UserProject[] = [];
  history: TaskHistory[] = [];
  inviteEmail = '';
  inviteRole: UserProject['role'] = 'MEMBRE';

  constructor(private api: ApiService) {}

  get userId(): number {
    return Number(localStorage.getItem('userId')) || 1;
  }

  // Fonction utilitaire pour éviter la duplication de code et les erreurs de référence
  getEmptyTask(projectId: number): Task {
    return {
      name: '',
      description: '',
      dueDate: '',
      idProject: projectId,
      priority: 1,
      assignedTo: ''
    };
  }

  createProject() {
    this.api.createProject(this.userId, this.project).subscribe(res => {
      this.currentProject = res;
      if (res.id) {
        this.newTask = this.getEmptyTask(res.id);
        this.loadTasks();
        this.loadMembers();
      }
    });
  }

  loadTasks() {
    if (this.currentProject?.id) {
      this.api.getTasksByProject(this.currentProject.id).subscribe(res => this.tasks = res);
    }
  }

  loadMembers() {
    if (this.currentProject?.id) {
      this.api.getProjectMembers(this.currentProject.id).subscribe(res => this.members = res);
    }
  }

  inviteMember() {
    if (!this.currentProject?.id || !this.inviteEmail) return;
    this.api.inviteMember(this.currentProject.id, this.inviteEmail, this.inviteRole).subscribe(() => {
      this.inviteEmail = '';
      this.loadMembers();
    });
  }

  createTask() {
    if (this.currentProject?.id) {
      this.api.createTask(this.currentProject.id, this.newTask).subscribe(() => {
        this.loadTasks();
        this.newTask = this.getEmptyTask(this.currentProject!.id!); // Reset du formulaire
      });
    }
  }

  markAsDone(task: Task) {
    if (!task.id) return;

    task.endDate = new Date().toISOString();
    task.status = 'DONE';
    this.api.updateTask(task.id, task).subscribe(() => this.loadTasks());
  }

  showHistory(task: Task) {
    if (task.id) this.api.getTaskHistory(task.id).subscribe(res => this.history = res);
  }
}
