package com.ecommerce.sb_ecom.Controller;

import com.ecommerce.sb_ecom.Config.AppConstants;
import com.ecommerce.sb_ecom.DTOModels.Product.ProductDTO;
import com.ecommerce.sb_ecom.DTOModels.Product.ProductResponse;
import com.ecommerce.sb_ecom.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                          @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                          @RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder,
                                                          @RequestParam(defaultValue =AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy
                                          ) {
        return new ResponseEntity<>(productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId,
                                                                @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                                @RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                                @RequestParam(defaultValue =AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
                                                                @RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        return new ResponseEntity<ProductResponse>(productService.getProductByCategory(categoryId, pageNumber, pageSize, sortBy, sortOrder), HttpStatus.OK);
    }

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDTO> saveProduct(@RequestBody @Valid ProductDTO productDTO, @PathVariable Long categoryId) {
        return new ResponseEntity<>(productService.saveProduct(productDTO, categoryId), HttpStatus.CREATED);
    }

}
