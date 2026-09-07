package fr.florobart.pmt.modules.tasksHistory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Date;

import org.junit.jupiter.api.Test;

import fr.florobart.pmt.modules.tasks.Task;
import fr.florobart.pmt.modules.tasks.TaskStatus;

class TaskHistoryTest {
    @Test
    void copiesAllTaskFields() {
        Date dueDate = new Date();
        Date endDate = new Date();
        Task task = new Task();
        task.setName("Task");
        task.setDescription("Description");
        task.setDueDate(dueDate);
        task.setEndDate(endDate);
        task.setPriority(3);
        task.setAssignedTo("user");
        task.setStatus(TaskStatus.DONE);

        TaskHistory history = TaskHistory.from(task);

        assertEquals("Task", history.getName());
        assertEquals("Description", history.getDescription());
        assertEquals(dueDate, history.getDueDate());
        assertEquals(endDate, history.getEndDate());
        assertEquals(3, history.getPriority());
        assertEquals("user", history.getAssignedTo());
        assertEquals(TaskStatus.DONE, history.getStatus());
        assertSame(task, history.getTask());
    }
}