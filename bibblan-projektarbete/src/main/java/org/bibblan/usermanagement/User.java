package org.bibblan.usermanagement;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class User {
    private String email;
    private String userName;
    private String name;
    private String password;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private final Integer ID;
}
