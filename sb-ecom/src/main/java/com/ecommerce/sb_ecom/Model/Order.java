package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.List;

public class Order {
    private Long orderId;
    private String email;
    private Date orderDate;
    private String orderStatus;
    private Double totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;
}
