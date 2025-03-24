package com.ecommerce.sb_ecom.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
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
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Role(AppRole roleName) {
        this.roleName = roleName;
    }

    // Méthode utilitaire pour gérer la relation bidirectionnelle
    public void addUser(User user) {
        this.users.add(user);
        user.getRoles().add(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId);
    }
}
