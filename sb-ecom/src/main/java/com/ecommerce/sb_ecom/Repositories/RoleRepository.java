package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.AppRole;
import com.ecommerce.sb_ecom.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}
