package com.ecommerce.sb_ecom.Repositories;

import com.ecommerce.sb_ecom.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
