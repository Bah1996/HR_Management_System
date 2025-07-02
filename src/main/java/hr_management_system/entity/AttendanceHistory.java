package hr_management_system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AttendanceHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    private boolean enterOrExit;

    private LocalDate date;

    private LocalTime time;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Attendance attendance;
}
