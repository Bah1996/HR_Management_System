package hr_management_system.repository;

import hr_management_system.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SalaryRepository extends JpaRepository<Salary, Integer> {

    List<Salary> findByUserIdAndMonthNumber(UUID userId, Integer month);
}
