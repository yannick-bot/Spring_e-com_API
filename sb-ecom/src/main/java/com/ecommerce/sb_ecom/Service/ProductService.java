package com.ecommerce.sb_ecom.Service;

import com.ecommerce.sb_ecom.DTOModels.Product.ProductDTO;
import com.ecommerce.sb_ecom.DTOModels.Product.ProductResponse;

public interface ProductService {
    ProductDTO saveProduct(ProductDTO productDTO, Long categoryId);

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    ProductResponse getProductByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

}
