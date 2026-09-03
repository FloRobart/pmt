package fr.florobart.pmt.modules.tasksHistory;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.florobart.pmt.modules.tasks.Task;
import fr.florobart.pmt.modules.tasks.TaskStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tasks_history")
public class TaskHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private Date dueDate;
    private Date endDate;
    private Integer priority;
    private String assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "id_task", nullable = false)
    @JsonIgnore
    private Task task;

    public TaskHistory() {}

    public static TaskHistory from(Task task) {
        TaskHistory history = new TaskHistory();
        history.name = task.getName();
        history.description = task.getDescription();
        history.dueDate = task.getDueDate();
        history.endDate = task.getEndDate();
        history.priority = task.getPriority();
        history.assignedTo = task.getAssignedTo();
        history.status = task.getStatus();
        history.task = task;
        return history;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Date getDueDate() { return dueDate; }
    public Date getEndDate() { return endDate; }
    public Integer getPriority() { return priority; }
    public String getAssignedTo() { return assignedTo; }
    public TaskStatus getStatus() { return status; }
    public Task getTask() { return task; }
}