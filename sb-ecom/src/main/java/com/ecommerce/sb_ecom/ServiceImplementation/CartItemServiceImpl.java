package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.Repositories.CartItemRepository;
import com.ecommerce.sb_ecom.Service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;
}
