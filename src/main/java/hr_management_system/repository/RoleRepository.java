package hr_management_system.repository;

import hr_management_system.entity.Role;
import hr_management_system.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
