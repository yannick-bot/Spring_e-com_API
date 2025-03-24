package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class OrderItem {
    private Long orderItemId;
    private Double discount;
    private Double orderedProductPrice;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
}
