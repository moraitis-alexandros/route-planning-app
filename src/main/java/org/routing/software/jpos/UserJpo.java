package org.routing.software.jpos;


import jakarta.persistence.*;
import lombok.*;
import org.routing.software.core.RoleType;

import java.security.Principal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class UserJpo extends AbstractEntity implements IdentifiableEntity, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING) // του λεω πως θα αποθηκευτει μεσα στη βαση αλλιως θα αποθηκευτει με το ordinal (1,2 κοκ)
    private RoleType roleType;

    @Override
    public String getName() {
        return username; // εδώ επιστρέφω αυτό που θέλω να αναγνωρίζει τον principal, πχ username
    }
}