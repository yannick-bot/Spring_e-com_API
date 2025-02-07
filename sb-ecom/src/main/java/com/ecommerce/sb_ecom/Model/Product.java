package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private Long productId;
    private String description;
    private double discount;
    private String image;
    private double price;
    private String productName;
    private Integer quantity;
    private double specialPrice;

}
