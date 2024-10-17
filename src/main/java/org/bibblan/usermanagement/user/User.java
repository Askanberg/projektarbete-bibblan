package org.bibblan.usermanagement.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Objects;

@Data
@Builder()
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
@ToString
public class User {

    @Email
    @NotBlank(message = "Email field is empty.")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Username field is empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters.")
    @Column(unique = true)
    private String username;

    @NotBlank
    private String name;

    @NotBlank(message = "Password field is empty.")
    @Size(min = 8, max = 40, message = ("Password must be between 8 and 40 characters."))
    @JsonIgnore
    private String password;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;


    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof User user)) return false;
        return Objects.equals(getID(), user.getID());
    }

    @Override
    public int hashCode(){
        return Objects.hash(getID());
    }
}
