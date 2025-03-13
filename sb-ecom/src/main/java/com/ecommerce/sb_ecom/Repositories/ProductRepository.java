package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.Category;
import com.ecommerce.sb_ecom.Model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductNameAndDescription(String productName, String description);

    Page<Product> findByCategory(Category category, Pageable pageRequest);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
