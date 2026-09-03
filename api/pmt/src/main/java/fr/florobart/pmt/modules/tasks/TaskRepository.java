package fr.florobart.pmt.modules.tasks;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProject_Id(Long projectId);
    List<Task> findByProject_IdAndStatus(Long projectId, TaskStatus status);
}