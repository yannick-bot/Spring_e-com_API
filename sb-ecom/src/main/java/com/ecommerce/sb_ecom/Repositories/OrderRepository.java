package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
