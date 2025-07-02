package hr_management_system.controller;

import hr_management_system.payload.ApiResponse;
import hr_management_system.payload.AttendanceDto;
import hr_management_system.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @PostMapping("/attendance")
    public HttpEntity<?> addAttendanceCard(@RequestBody AttendanceDto attendanceDto){
        ApiResponse apiResponse = attendanceService.addAttendance(attendanceDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200 : 409).body(apiResponse);
    }
}
