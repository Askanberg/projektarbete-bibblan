package org.bibblan.usermanagement.role;

import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode
@RequiredArgsConstructor
@Entity(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer ID;

    @NonNull
    @Column(unique = true, nullable = false)
    private String name;

    public Role() {

    }
}
