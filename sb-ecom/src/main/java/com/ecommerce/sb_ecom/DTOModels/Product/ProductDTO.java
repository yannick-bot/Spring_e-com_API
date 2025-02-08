package com.ecommerce.sb_ecom.DTOModels.Product;

import com.ecommerce.sb_ecom.Model.Category;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long productId;
    @NotBlank
    private String description;
    private double discount;
    private String image;
    private double price;
    @NotBlank
    private String productName;
    private Integer quantity;
    private double specialPrice;

    private Category category;
}
