package com.ecommerce.sb_ecom.Service;

import com.ecommerce.sb_ecom.DTOModels.Cart.CartDTO;
import com.ecommerce.sb_ecom.DTOModels.Cart.CartResponse;

public interface CartService{

    CartResponse getAllCarts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CartDTO getUserCart(String username);

    CartDTO updateProductQuantity(Long productId, Integer quantity);

    String deleteProductFromCart(Long cartId, Long productId);

    CartDTO addProductToCart(Long productId, String name);
}
