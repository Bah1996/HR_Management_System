package hr_management_system.service;

import hr_management_system.entity.Attendance;
import hr_management_system.entity.AttendanceHistory;
import hr_management_system.payload.ApiResponse;
import hr_management_system.payload.AttendanceHistoryDto;
import hr_management_system.repository.AttendanceHistoryRepository;
import hr_management_system.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AttendanceHistoryService {

    @Autowired
    AttendanceHistoryRepository attendanceHistoryRepository;
    @Autowired
    AttendanceRepository attendanceRepository;


    public ApiResponse enterOrExit(AttendanceHistoryDto attendanceHistoryDto){
        Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceHistoryDto.getAttendanceId());
        if (optionalAttendance.isEmpty())
            return new ApiResponse("Attendance not found", false);

        AttendanceHistory attendanceHistory = new AttendanceHistory();
        attendanceHistory.setEnterOrExit(attendanceHistoryDto.isEnterOrExit());
        attendanceHistory.setAttendance(optionalAttendance.get());

        if (attendanceHistoryDto.isEnterOrExit()){
            attendanceHistory.setDate(LocalDate.now());
            attendanceHistory.setTime(LocalTime.now());
            attendanceHistoryRepository.save(attendanceHistory);
            return new ApiResponse("The employee " + optionalAttendance.get().getUser().getFirstName() + " " +
                    optionalAttendance.get().getUser().getLastName() + " entered at " + attendanceHistory.getDate() + " " +
                    attendanceHistory.getTime(), true);
        }
        else {
            attendanceHistory.setDate(LocalDate.now());
            attendanceHistory.setTime(LocalTime.now());
            attendanceHistoryRepository.save(attendanceHistory);
            return new ApiResponse("The employee " + optionalAttendance.get().getUser().getFirstName() + " " +
                    optionalAttendance.get().getUser().getLastName() + " exited at " + attendanceHistory.getDate() + " " +
                    attendanceHistory.getTime(), true);
        }
    }
}
