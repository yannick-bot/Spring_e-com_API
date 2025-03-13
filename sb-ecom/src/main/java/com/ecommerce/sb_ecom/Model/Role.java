package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.*;

@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "role_id")
    private Integer roleId;

    @Enumerated(EnumType.STRING) // pour que le role soit persisté en tant que string et non integer
    @Column(name = "role_name")
    private AppRole roleName;
}
