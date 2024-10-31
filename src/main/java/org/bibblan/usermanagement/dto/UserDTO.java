package org.bibblan.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
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

    @NotBlank(message = "Email field is empty.")
    @Email(message = "Invalid email format.")
    @Size(min=5, max=254, message = ("Email must be between 5 and 254 characters."))
    private String email;

    @NonNull
    @NotBlank(message = "Username field is empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    private String username;

    @NonNull
    @NotBlank(message = "Name field is empty.")
    private String name;

    @NotBlank(message = "Password field is empty.")
    @Size(min = 8, max = 40, message = ("Password must be between 8 and 40 characters."))
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

}
