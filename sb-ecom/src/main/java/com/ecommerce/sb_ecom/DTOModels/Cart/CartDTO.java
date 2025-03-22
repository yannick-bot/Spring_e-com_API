package com.ecommerce.sb_ecom.DTOModels.Cart;

import com.ecommerce.sb_ecom.DTOModels.Product.ProductDTO;
import com.ecommerce.sb_ecom.DTOModels.User.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long cartId;
    private double totalPrice;

    private UserDTO user;
    private List<ProductDTO> items = new ArrayList<>();
}
