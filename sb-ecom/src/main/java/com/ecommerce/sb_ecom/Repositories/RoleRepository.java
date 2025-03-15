package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
