package com.ecommerce.sb_ecom.Controller;

import com.ecommerce.sb_ecom.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;
}
