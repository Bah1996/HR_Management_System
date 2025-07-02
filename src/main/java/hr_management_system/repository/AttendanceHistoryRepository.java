package hr_management_system.repository;

import hr_management_system.entity.AttendanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AttendanceHistoryRepository extends JpaRepository<AttendanceHistory, UUID> {

    List<AttendanceHistory> findAllByAttendanceUserId(UUID attendance_user_id);

    @Query(value = "select * from attendance_history join " +
            "attendance a on a.id = attendance_history.attendance_id " +
            "where (a.user_id= :userId and attendance_history.date= :date)", nativeQuery = true)
    List<AttendanceHistory> findByDateAndUserId(LocalDate date, UUID userId);

    boolean existsByDate(LocalDate date);
}
