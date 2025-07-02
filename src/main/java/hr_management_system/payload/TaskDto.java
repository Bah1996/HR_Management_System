package hr_management_system.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    @NotNull
    private String taskName;

    @NotNull
    private String description;

    @NotNull
    private Timestamp deadline;

    @NotNull
    private UUID employeeId;
}
