package hr_management_system.payload;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryDto {

    @NotNull
    private UUID toUserId;

    @NotNull
    private Double monthlyAmount;

    @NotNull
    private Integer monthNumber;

    @NotNull
    private boolean isPaid;
}
