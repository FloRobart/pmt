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

  it('devrait afficher le formulaire de création de projet', () => {
    const fixture = TestBed.createComponent(WorkspaceComponent);

    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('h2').textContent).toContain('Nouveau Projet');
    expect(fixture.nativeElement.querySelector('input[name="name"]')).not.toBeNull();
    expect(fixture.nativeElement.querySelector('textarea[name="desc"]')).not.toBeNull();
  });

  it('devrait afficher le tableau de bord avec les tâches et membres', () => {
    const fixture = TestBed.createComponent(WorkspaceComponent);
    const component = fixture.componentInstance;
    component.currentProject = { id: 4, name: 'Projet', description: 'Description', startDate: '' };
    component.members = [{ id: { userId: 12, projectId: 4 }, role: 'MEMBRE' }];
    component.tasks = [
      { id: 9, name: 'Tâche en cours', description: 'A faire', dueDate: '2026-01-01', idProject: 4 },
      { id: 10, name: 'Tâche terminée', description: '', dueDate: '', endDate: '2026-01-02', idProject: 4 }
    ];
    component.history = [{ name: 'Ancienne tâche', status: 'DONE', priority: 1 }];

    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('h2').textContent).toContain('Projet : Projet');
    expect(fixture.nativeElement.textContent).toContain('Utilisateur #12');
    expect(fixture.nativeElement.textContent).toContain('Tâche en cours');
    expect(fixture.nativeElement.textContent).toContain('Tâche terminée');
    expect(fixture.nativeElement.textContent).toContain('Ancienne tâche');
  });

  it('devrait créer une tâche puis réinitialiser le formulaire', () => {
    const component = TestBed.createComponent(WorkspaceComponent).componentInstance;
    component.currentProject = { id: 4, name: 'Projet', description: '', startDate: '' };
    component.newTask = { name: 'Nouvelle', description: 'Description', dueDate: '', idProject: 4 };
    api.createTask.mockReturnValue(of(component.newTask));
    api.getTasksByProject.mockReturnValue(of([]));

    component.createTask();

    expect(api.createTask).toHaveBeenCalledWith(4, expect.objectContaining({ name: 'Nouvelle' }));
    expect(component.newTask).toEqual(component.getEmptyTask(4));
    expect(api.getTasksByProject).toHaveBeenCalledWith(4);
  });

  it('devrait ignorer les actions impossibles sans projet ou sans identifiant', () => {
    const component = TestBed.createComponent(WorkspaceComponent).componentInstance;
    const task: Task = { name: 'Tâche', description: '', dueDate: '', idProject: 0 };

    component.loadTasks();
    component.loadMembers();
    component.inviteMember();
    component.createTask();
    component.markAsDone(task);
    component.showHistory(task);

    expect(api.getTasksByProject).not.toHaveBeenCalled();
    expect(api.getProjectMembers).not.toHaveBeenCalled();
    expect(api.inviteMember).not.toHaveBeenCalled();
    expect(api.createTask).not.toHaveBeenCalled();
    expect(api.updateTask).not.toHaveBeenCalled();
    expect(api.getTaskHistory).not.toHaveBeenCalled();
  });

  it('devrait ignorer une invitation sans adresse email', () => {
    const component = TestBed.createComponent(WorkspaceComponent).componentInstance;
    component.currentProject = { id: 4, name: 'Projet', description: '', startDate: '' };

    component.inviteMember();

    expect(api.inviteMember).not.toHaveBeenCalled();
  });
});
