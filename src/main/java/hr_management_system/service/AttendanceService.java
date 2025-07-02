package hr_management_system.service;

import hr_management_system.entity.Attendance;
import hr_management_system.entity.Role;
import hr_management_system.entity.User;
import hr_management_system.payload.ApiResponse;
import hr_management_system.payload.AttendanceDto;
import hr_management_system.repository.AttendanceRepository;
import hr_management_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class AttendanceService {

    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    UserRepository userRepository;

    //ADD CARD FOR EMPLOYEE
    public ApiResponse addAttendance(AttendanceDto attendanceDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles){
            if (role.getName().name().equals("MANAGER") || role.getName().name().equals("EMPLOYEE")){
                return new ApiResponse("Only Director and HR_Manager can add Card", false);
            }
        }

        Optional<User> optionalUser = userRepository.findById(attendanceDto.getUserId());
        if (optionalUser.isEmpty()){
            return new ApiResponse("User not found", false);
        }

        boolean existsByUserId = attendanceRepository.existsByUserId(attendanceDto.getUserId());
        if (existsByUserId)
            return new ApiResponse("Attendance for this User already exist", false);

        Attendance attendance = new Attendance();
        attendance.setUser(optionalUser.get());
        attendanceRepository.save(attendance);
        return new ApiResponse("Attendance added succesfully", true);
    }
}













