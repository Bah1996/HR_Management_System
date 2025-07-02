package hr_management_system.repository;

import hr_management_system.entity.Task;
import hr_management_system.entity.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<Task> findByIdAndToUser_Email(Integer id, String userEmail);

    List<Task> findAllByTaskStatusAndToUser_Id(TaskStatus taskStatus, UUID toUserId);
}
