package hr_management_system.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceHistoryDto {

    @NotNull
    private boolean enterOrExit;

    @NotNull
    private UUID AttendanceId;
}
