import { Task } from './task.model';

describe('Task model', () => {
  it('devrait accepter une tâche assignée en cours', () => {
    const task: Task = {
      id: 2,
      name: 'Tâche',
      description: 'Description',
      dueDate: '2026-01-15',
      idProject: 1,
      priority: 2,
      assignedTo: 'user@example.com',
      status: 'IN_PROGRESS'
    };

    expect(task.status).toBe('IN_PROGRESS');
    expect(task.idProject).toBe(1);
  });

  it('devrait accepter une tâche terminée avec une date de fin', () => {
    const task: Task = {
      name: 'Tâche terminée',
      description: '',
      dueDate: new Date(2026, 0, 15),
      endDate: new Date(2026, 0, 10),
      idProject: 1,
      status: 'DONE'
    };

    expect(task.endDate).toBeInstanceOf(Date);
    expect(task.status).toBe('DONE');
  });
});
