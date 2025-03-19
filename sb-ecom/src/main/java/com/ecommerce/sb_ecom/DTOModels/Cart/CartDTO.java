package com.ecommerce.sb_ecom.DTOModels.Cart;

import com.ecommerce.sb_ecom.Model.CartItem;
import com.ecommerce.sb_ecom.Model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDTO {
    private Long cartId;
    private double totalPrice;

    private User user;
}
