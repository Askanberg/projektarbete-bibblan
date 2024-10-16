package org.bibblan.usermanagement.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Data
@Builder()
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {
    private String email;

    @Column(unique = true)
    private String userName;
    private String name;
    private String password;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer ID;

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", ID=" + ID +
                '}';
    }

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
