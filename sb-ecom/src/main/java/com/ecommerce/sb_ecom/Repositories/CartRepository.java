package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.Cart;
import com.ecommerce.sb_ecom.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUser(User user);
}
