package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);

}
