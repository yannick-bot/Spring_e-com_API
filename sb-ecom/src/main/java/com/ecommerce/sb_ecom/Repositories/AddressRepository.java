package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.Address;
import com.ecommerce.sb_ecom.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("SELECT a FROM Address a JOIN a.users u WHERE u = :user")
    List<Address> findAllByUser(@Param("user") User user);
}
