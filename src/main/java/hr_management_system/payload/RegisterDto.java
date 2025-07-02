package hr_management_system.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterDto {

    @NotNull
    @Size(min = 3, max = 50)
    private String firstName;

    @NotNull
    @Length(min = 3, max = 50)
    private String lastName;

    @NotNull
    @Email
    private String email;

    @NotNull
    private String password;
}
