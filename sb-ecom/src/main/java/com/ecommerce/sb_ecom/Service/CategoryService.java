package com.ecommerce.sb_ecom.Service;

import com.ecommerce.sb_ecom.DTOModels.CategoryDTO;
import com.ecommerce.sb_ecom.DTOModels.CategoryResponse;


public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO category);
    CategoryDTO  deleteCategory(Long categoryId);
}
