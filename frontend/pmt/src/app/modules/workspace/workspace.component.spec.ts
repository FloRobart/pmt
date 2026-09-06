import { TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ApiService } from '../../core/services/api.service';
import { Task } from '../../core/models/task.model';
import { WorkspaceComponent } from './workspace.component';

describe('WorkspaceComponent', () => {
  const api = {
    createProject: vi.fn(),
    getTasksByProject: vi.fn(),
    getProjectMembers: vi.fn(),
    inviteMember: vi.fn(),
    createTask: vi.fn(),
    updateTask: vi.fn(),
    getTaskHistory: vi.fn()
  };

  beforeEach(async () => {
    vi.clearAllMocks();
    localStorage.clear();
    await TestBed.configureTestingModule({
      imports: [WorkspaceComponent],
      providers: [{ provide: ApiService, useValue: api }]
    }).compileComponents();
  });

  it('devrait utiliser l utilisateur courant et créer une tâche vide', () => {
    localStorage.setItem('userId', '12');
    const component = TestBed.createComponent(WorkspaceComponent).componentInstance;

    expect(component.userId).toBe(12);
    expect(component.getEmptyTask(4)).toEqual({
      name: '', description: '', dueDate: '', idProject: 4, priority: 1, assignedTo: ''
    });
  });

  it('devrait créer un projet puis charger ses tâches et membres', () => {
    const component = TestBed.createComponent(WorkspaceComponent).componentInstance;
    const project = { id: 4, name: 'Projet', description: 'Description', startDate: '2026-01-01' };
    api.createProject.mockReturnValue(of(project));
    api.getTasksByProject.mockReturnValue(of([]));
    api.getProjectMembers.mockReturnValue(of([]));
    component.project = project;

    component.createProject();

    expect(api.createProject).toHaveBeenCalledWith(1, project);
    expect(component.currentProject).toEqual(project);
    expect(component.newTask.idProject).toBe(4);
    expect(api.getTasksByProject).toHaveBeenCalledWith(4);
    expect(api.getProjectMembers).toHaveBeenCalledWith(4);
  });

  it('devrait inviter un membre et recharger la liste', () => {
    const component = TestBed.createComponent(WorkspaceComponent).componentInstance;
    component.currentProject = { id: 4, name: 'Projet', description: '', startDate: '' };
    component.inviteEmail = 'member@example.com';
    api.inviteMember.mockReturnValue(of({}));
    api.getProjectMembers.mockReturnValue(of([]));

    component.inviteMember();

    expect(api.inviteMember).toHaveBeenCalledWith(4, 'member@example.com', 'MEMBRE');
    expect(component.inviteEmail).toBe('');
    expect(api.getProjectMembers).toHaveBeenCalledWith(4);
  });

  it('devrait terminer une tâche et recharger les tâches', () => {
    const component = TestBed.createComponent(WorkspaceComponent).componentInstance;
    component.currentProject = { id: 4, name: 'Projet', description: '', startDate: '' };
    const task: Task = { id: 9, name: 'Tâche', description: '', dueDate: '', idProject: 4 };
    api.updateTask.mockReturnValue(of(task));
    api.getTasksByProject.mockReturnValue(of([]));

    component.markAsDone(task);

    expect(task.status).toBe('DONE');
    expect(task.endDate).toEqual(expect.any(String));
    expect(api.updateTask).toHaveBeenCalledWith(9, task);
    expect(api.getTasksByProject).toHaveBeenCalledWith(4);
  });

  it('devrait charger l historique d une tâche', () => {
    const component = TestBed.createComponent(WorkspaceComponent).componentInstance;
    const history = [{ name: 'Tâche', status: 'DONE' as const, priority: 1 }];
    api.getTaskHistory.mockReturnValue(of(history));

    component.showHistory({ id: 9, name: 'Tâche', description: '', dueDate: '', idProject: 4 });

    expect(api.getTaskHistory).toHaveBeenCalledWith(9);
    expect(component.history).toEqual(history);
  });
});