package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.CartItem;
import com.ecommerce.sb_ecom.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByProduct(Product product);

    List<CartItem> findAllByProduct(Product product);
}
