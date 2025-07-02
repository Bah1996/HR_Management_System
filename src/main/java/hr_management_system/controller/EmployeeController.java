package hr_management_system.controller;

import hr_management_system.entity.AttendanceHistory;
import hr_management_system.payload.ApiResponse;
import hr_management_system.payload.SalaryDto;
import hr_management_system.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/allEmployees")
    public HttpEntity<?> getAllEmployees(){
    ApiResponse allEmployee = employeeService.getAllEmployee();
    return ResponseEntity.ok(allEmployee);
    }

    @GetMapping("/byAttendance/{id}")
    public HttpEntity<?> getAllAttendanceByEmployee(@PathVariable UUID id){
        ApiResponse allAttendanceByEmployee = employeeService.getAllAttendanceForOneEmployee(id);
        return ResponseEntity.status(allAttendanceByEmployee.isSuccess() ? 200:409).body(allAttendanceByEmployee);
    }

    @GetMapping("allAttendance")
    public HttpEntity<?> getAllAttendance(){
        ApiResponse apiResponse = employeeService.getAllAttendanceByEmployee();
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/byTaskStatus/{id}")
    public HttpEntity<?> employeesByTaskStatus(@PathVariable UUID id){
        ApiResponse employeesByCompletedTask = employeeService.getEmployeesByCompletedTask(id);
        return ResponseEntity.status(employeesByCompletedTask.isSuccess() ? 200:409).body(employeesByCompletedTask);
    }

    @PostMapping("/paySalary")
    public HttpEntity<?> paySalary(@RequestBody SalaryDto salaryDto){
        ApiResponse apiResponse = employeeService.paySalaryToEmployee(salaryDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getSalaryByUserIdAndMonth/{id}")
    public HttpEntity<?> getSalaryByUserIdAndMonth(@PathVariable UUID id, @RequestBody SalaryDto salaryDto){
        ApiResponse salariesByUserIdAndMonth = employeeService.getSalariesByUserIdAndMonth(id, salaryDto);
        return ResponseEntity.status(salariesByUserIdAndMonth.isSuccess() ? 200 : 409).body(salariesByUserIdAndMonth);
    }

    @GetMapping("/tasksAndUsers")
    public HttpEntity<?> getTasks() {
        ApiResponse tasksAndUsers = employeeService.getTasks();
        return ResponseEntity.ok(tasksAndUsers);
    }

    @GetMapping("/getTurniketHistoryByDate/{id}")
    public HttpEntity<?> getAttendanceHistoryByDate(@RequestBody AttendanceHistory attendanceHistory, @PathVariable UUID id) {
        ApiResponse apiResponse = employeeService.getAttendanceHistoryByDate(attendanceHistory, id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }
}
