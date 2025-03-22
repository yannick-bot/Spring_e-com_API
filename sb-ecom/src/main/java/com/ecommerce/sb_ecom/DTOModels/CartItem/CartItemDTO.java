package com.ecommerce.sb_ecom.DTOModels.CartItem;

import com.ecommerce.sb_ecom.DTOModels.Cart.CartDTO;
import com.ecommerce.sb_ecom.DTOModels.Product.ProductDTO;
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

    private CartDTO cart;

    private ProductDTO product;
}
