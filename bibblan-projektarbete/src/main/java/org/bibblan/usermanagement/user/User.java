package org.bibblan.usermanagement.user;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

@Getter
@Setter
@Builder()
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {
    private String email;
    private String userName;
    private String name;
    private String password;

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private final Integer ID;

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
        if(!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getID(), user.getID());
    }

    @Override
    public int hashCode(){
        return Objects.hash(getID());
    }
}