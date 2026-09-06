import { Project } from './project.model';

describe('Project model', () => {
  it('devrait accepter un projet complet avec une date texte', () => {
    const project: Project = {
      id: 1,
      name: 'Projet',
      description: 'Description',
      startDate: '2026-01-01'
    };

    expect(project).toMatchObject({ name: 'Projet', description: 'Description', startDate: '2026-01-01' });
  });

  it('devrait accepter une date de début JavaScript', () => {
    const project: Project = { name: 'Projet', description: '', startDate: new Date(2026, 0, 1) };

    expect(project.startDate).toBeInstanceOf(Date);
  });
});
