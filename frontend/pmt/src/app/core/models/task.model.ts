export interface Task {
    id?: number;
    name: string;
    description: string;
    dueDate: string | Date;
    endDate?: string | Date;
    priority?: number;
    idProject: number;
    assignedTo?: string;
    status?: 'TODO' | 'IN_PROGRESS' | 'DONE';
}