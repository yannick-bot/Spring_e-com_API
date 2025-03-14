package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    @NotBlank
    @Size(min = 6)
    private String description;
    private double discount;
    private String image;
    private double price;
    @NotBlank
    @Size(min = 3)
    private String productName;
    private Integer quantity;
    private double specialPrice;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryID")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

}
