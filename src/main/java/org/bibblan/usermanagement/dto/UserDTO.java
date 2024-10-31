package org.bibblan.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserDTO {

    @NotBlank(message = "Email field is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NonNull
    @NotBlank(message = "Invalid username.")
    @Pattern(regexp = "\\S+", message = "Invalid username.")
    @Size(min = 3, max = 20, message = "Invalid username.")
    private String username;

    @NonNull
    @NotBlank(message = "Name field is required.")
    private String name;

    @NotBlank(message = "Password field is required.")
    @Size(min = 8, max = 40, message = "Password must be between 8 and 40 characters.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
