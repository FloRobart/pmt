-- users (id, username, email, password)
CREATE TABLE users (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL
);


-- projects (id, name, description, start_date)
CREATE TABLE projects (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL
);

-- tasks (id, name, description, due_date, end_date, priority, status, id_project, assigned_to)
CREATE TABLE tasks (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    due_date DATE NOT NULL,
    end_date DATE,
    priority INTEGER NOT NULL DEFAULT 0,
    id_project INTEGER NOT NULL REFERENCES projects(id),
    status VARCHAR(30) NOT NULL DEFAULT 'TODO',
    assigned_to VARCHAR(255)
);

-- tasks_history (id, name, description, due_date, end_date, priority, status, assigned_to, id_task)
CREATE TABLE tasks_history (
    id INTEGER PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    due_date DATE NOT NULL,
    end_date DATE,
    priority INTEGER,
    status VARCHAR(30) NOT NULL DEFAULT 'TODO',
    assigned_to VARCHAR(255),
    id_task INTEGER NOT NULL REFERENCES tasks(id)
);

-- users_projects (id_user, id_project, role)
CREATE TABLE users_projects (
    id_user INTEGER NOT NULL REFERENCES users(id),
    id_project INTEGER NOT NULL REFERENCES projects(id),
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (id_user, id_project)
);

INSERT INTO users (username, email, password) VALUES
    ('Demo User', 'demo@pmt.local', 'demo');

INSERT INTO projects (name, description, start_date) VALUES
    ('Projet de démonstration', 'Projet de test PMT', CURRENT_DATE);

INSERT INTO users_projects (id_user, id_project, role) VALUES
    (1, 1, 'ADMINISTRATEUR');
