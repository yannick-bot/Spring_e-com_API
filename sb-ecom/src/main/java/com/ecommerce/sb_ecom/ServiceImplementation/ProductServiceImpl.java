package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.Repositories.ProductRepository;
import com.ecommerce.sb_ecom.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;
}
