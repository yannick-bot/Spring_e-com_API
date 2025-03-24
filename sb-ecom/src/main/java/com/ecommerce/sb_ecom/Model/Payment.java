package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;

import java.util.List;

public class Payment {
    private Long paymentId;
    private String paymentMethod;

    @OneToMany(mappedBy = "payment", cascade = CascadeType.ALL)
    private List<Order> orders;
}
