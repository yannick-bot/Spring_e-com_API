package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
