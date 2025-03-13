package com.ecommerce.sb_ecom.Service;

import com.ecommerce.sb_ecom.DTOModels.Category.CategoryDTO;
import com.ecommerce.sb_ecom.DTOModels.Category.CategoryResponse;


public interface CategoryService {

    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO category);
    CategoryDTO  deleteCategory(Long categoryId);

    CategoryDTO getOneCategory(Long categoryId);
}
