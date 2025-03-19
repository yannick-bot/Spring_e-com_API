package com.ecommerce.sb_ecom.DTOModels.CartItem;

import com.ecommerce.sb_ecom.Model.Cart;
import com.ecommerce.sb_ecom.Model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private double discount;
    private Double product_price;
    private Integer quantity;

    private Cart cart;

    private Product product;
}
