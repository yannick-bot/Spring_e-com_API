package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private Long orderId;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private LocalDate orderDate;
    @NotBlank
    private String orderStatus;
    private Double totalAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;


    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
}
