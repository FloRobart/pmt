import { User } from './user.model';

describe('User model', () => {
  it('devrait accepter un utilisateur authentifiable', () => {
    const user: User = {
      id: 3,
      username: 'Alice',
      email: 'alice@example.com',
      password: 'secret'
    };

    expect(user).toEqual({ id: 3, username: 'Alice', email: 'alice@example.com', password: 'secret' });
  });

  it('devrait accepter un utilisateur sans identifiant ni nom facultatifs', () => {
    const user: User = { email: 'anonymous@example.com' };

    expect(user.email).toBe('anonymous@example.com');
    expect(user.id).toBeUndefined();
    expect(user.username).toBeUndefined();
  });
});