package hr_management_system.service;

import com.bek.hr_management_system.entity.*;
import hr_management_system.entity.*;
import hr_management_system.entity.enums.TaskStatus;
import hr_management_system.payload.ApiResponse;
import hr_management_system.payload.SalaryDto;
import com.bek.hr_management_system.repository.*;
import hr_management_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class EmployeeService {

    @Autowired
    AttendanceRepository attendanceRepository;
    @Autowired
    AttendanceHistoryRepository attendanceHistoryRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    SalaryRepository salaryRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

    //GET ALL EMPLOYEES FOR  DIRECTOR OR MANAGER
    public ApiResponse getAllEmployee(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles){
            if (role.getRoleName().name().equals("DIRECTOR") || role.getRoleName().name().equals("HR_MANAGER")){
                List<User> employeeList = userRepository.findAll();
                return new ApiResponse("List of all Employees", true, employeeList);
            }
            return new ApiResponse("Only Director or HR_Manager can see the employee list", false);
        }
        return new ApiResponse("Cannot get the list", false);
    }

    //GET ATTENDANCE LIST FOR ONE EMPLOYEE
    public ApiResponse getAllAttendanceForOneEmployee(UUID id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles){
            if (role.getRoleName().name().equals("MANAGER") || role.getRoleName().name().equals("EMPLOYEE")){
                return new ApiResponse("Director and HR_Manager can get Attendance List", false);
            }
        }
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()){
            return new ApiResponse("User not found", false);
        }
        List<AttendanceHistory> attendanceHistoryList = attendanceHistoryRepository.findAllByAttendanceUserId(id);
        if (attendanceHistoryList.isEmpty()){
            return new ApiResponse("Attendance History not found", false);
        }
        return new ApiResponse("Attendance History List: ", true, attendanceHistoryList);
    }

    //GET ATTENDANCE LIST BY EMPLOYEE
    public ApiResponse getAllAttendanceByEmployee(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles){
            if (role.getRoleName().name().equals("MANAGER") || role.getRoleName().name().equals("EMPLOYEE")){
                return new ApiResponse("Only Director and HR_Manager can get List", false);
            }
        }
        List<AttendanceHistory> attendanceHistoryList = attendanceHistoryRepository.findAll();
        if (attendanceHistoryList.isEmpty()){
            return new ApiResponse("User Attendance not found", false);
        }
        return new ApiResponse("Attendance List: ", true, attendanceHistoryList);
    }

    //GET COMPLETED TASKS
    public ApiResponse getEmployeesByCompletedTask(UUID toUserId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role:roles){
            if (!role.getRoleName().name().equals("DIRECTOR")){
                return new ApiResponse("Only Director can see this list", false);
            }
        }
        Optional<User> optionalUser = userRepository.findById(toUserId);
        if (optionalUser.isEmpty())
            return new ApiResponse("User not found", false);

        List<Task> allByTaskStatusAndToUserId = taskRepository.findAllByTaskStatusAndToUser_Id(TaskStatus.COMPLETED, toUserId);
        if (allByTaskStatusAndToUserId.isEmpty())
            return new ApiResponse("Information not found", false);
        return new ApiResponse("Users list by their Task: ", true, allByTaskStatusAndToUserId);
    }

    //CALCULATING SALARY TO THE EMPLOYEES
    public ApiResponse paySalaryToEmployee(SalaryDto salaryDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role:roles){
            if (!role.getRoleName().name().equals("DIRECTOR")){
                return new ApiResponse("Only Director can pay the Salary", false);
            }
        }

        Optional<User> optionalUser = userRepository.findById(salaryDto.getToUserId());
        if (optionalUser.isEmpty())
            return new ApiResponse("User not found", false);

        Salary salary = new Salary();
        salary.setUser(optionalUser.get());
        salary.setMonthlyAmount(salaryDto.getMonthlyAmount());

        if (salaryDto.getMonthNumber() > 12){
            return new ApiResponse("No month with this Number", false);
        }

        salary.setMonthNumber(salaryDto.getMonthNumber());
        salary.setPaid(salaryDto.isPaid());

        salaryRepository.save(salary);
        return new ApiResponse("Salary paid to " + optionalUser.get().getFirstName() + " " + optionalUser.get().getLastName(), true);
    }

    //GET SALARY OF EMPLOYEES BY MONTH AND USER ID
    public ApiResponse getSalariesByUserIdAndMonth(UUID id, SalaryDto salaryDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role:roles){
            if (!role.getRoleName().name().equals("DIRECTOR")){
                return new ApiResponse("Only Director can see the Salary", false);
            }
        }

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty())
            return new ApiResponse("User not found", false);

        if (salaryDto.getMonthNumber() > 12){
            return new ApiResponse("No month with this Number", false);
        }

        List<Salary> salaryList = salaryRepository.findByUserIdAndMonthNumber(id, salaryDto.getMonthNumber());
        if (salaryList.isEmpty())
            return new ApiResponse("Salary information not found", false);

        return new ApiResponse("This is the Salary of the user " + optionalUser.get().getFirstName(), true, salaryList);
    }

    //GET LIST OF TASKS
    public ApiResponse getTasks(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role:roles){
            if (!role.getRoleName().name().equals("EMPLOYEE")){
                return new ApiResponse("Emplyees can not get Tasks", false);
            }
        }
        List<Task> taskList = taskRepository.findAll();
        return new ApiResponse("List of Tasks: ", true, taskList);
    }

    //GET ATTENDANCE HISTORY BY DATE
    public ApiResponse getAttendanceHistoryByDate(AttendanceHistory attendanceHistory, UUID userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            if (role.getRoleName().name().equals("MANAGER") || role.getRoleName().name().equals("WORKER")) {
                return new ApiResponse("Only director and hr manager can see this list", false);
            }
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return new ApiResponse("User with this id is not found", false);
        }

        boolean existsByDate = attendanceHistoryRepository.existsByDate(attendanceHistory.getDate());
        if (!existsByDate)
            return new ApiResponse("Attendance History in this date not exist", false);

        List<AttendanceHistory> byDateAndUserId = attendanceHistoryRepository.findByDateAndUserId(attendanceHistory.getDate(), userId);
        return new ApiResponse("Following are the attendance of " + optionalUser.get().getFirstName(), true, byDateAndUserId);
    }
}
