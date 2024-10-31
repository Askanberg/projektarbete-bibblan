package org.bibblan.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Email
    @NonNull
    @NotBlank(message = "Email field is empty.")
    @Size(min=5, max=254)
    @Pattern(regexp = "^([a-zA-Z0-9._-]+@[a-zA-Z]+[.][a-zA-Z]{2,3})$")
    @Column(unique = true)
    private String email;

    @NonNull
    @NotBlank(message = "Username field is empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    @Column(unique = true)
    private String username;

    @NonNull
    @NotBlank
    private String name;

    @NotBlank(message = "Password field is empty.")
    @Size(min = 8, max = 40, message = ("Password must be between 8 and 40 characters."))
    @JsonIgnore
    private String password;

}
