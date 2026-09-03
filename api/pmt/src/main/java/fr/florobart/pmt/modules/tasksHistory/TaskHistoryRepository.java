package fr.florobart.pmt.modules.tasksHistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, Long> {
    List<TaskHistory> findByTask_IdOrderByIdDesc(Long taskId);
}