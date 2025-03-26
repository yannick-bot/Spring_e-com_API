package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
