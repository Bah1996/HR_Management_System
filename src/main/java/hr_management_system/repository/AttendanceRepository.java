package hr_management_system.repository;

import hr_management_system.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {

    boolean existsByUserId(UUID userId);
}
