package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "roleId")
    private Integer roleId;

    @ToString.Exclude
    @Enumerated(EnumType.STRING) // pour que le role soit persisté en tant que string et non integer
    @Column(name = "role_name")
    private AppRole roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;
}
