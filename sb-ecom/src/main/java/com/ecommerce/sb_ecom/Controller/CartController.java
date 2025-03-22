package com.ecommerce.sb_ecom.Controller;

import com.ecommerce.sb_ecom.Config.AppConstants;
import com.ecommerce.sb_ecom.DTOModels.Cart.CartDTO;
import com.ecommerce.sb_ecom.DTOModels.Cart.CartResponse;
import com.ecommerce.sb_ecom.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/carts")
    public ResponseEntity<CartResponse> getAllCarts(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                    @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                    @RequestParam(defaultValue = AppConstants.SORT_CARTS_BY, required = false) String sortBy,
                                                    @RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        return new ResponseEntity<>(cartService.getAllCarts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }


    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getUserCart(Authentication authentication) {
        return new ResponseEntity<>(cartService.getUserCart(authentication.getName()), HttpStatus.OK);
    }

    @PutMapping("/carts/products/{productId}/quantity/{quantity}/cartItems")
    public ResponseEntity<CartDTO> updateProductQuantity(@PathVariable Long productId, @PathVariable Integer quantity) {
        return new ResponseEntity<>(cartService.updateProductQuantity(productId, quantity), HttpStatus.OK);
    }

    @PostMapping("/carts/products/{productId}/cartItems")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId, Authentication authentication) {
        return new ResponseEntity<>(cartService.addProductToCart(productId, authentication.getName()), HttpStatus.OK);
    }


    @DeleteMapping("/carts/{cartId}/products/{productId}")
    public ResponseEntity<String> deleteProductFromCart(@PathVariable Long productId, @PathVariable Long cartId) {
        return new ResponseEntity<>(cartService.deleteProductFromCart(cartId, productId), HttpStatus.OK);
    }
}
