package com.ecommerce.sb_ecom.Service;

import com.ecommerce.sb_ecom.DTOModels.CategoryDTO;
import com.ecommerce.sb_ecom.DTOModels.CategoryResponse;
import com.ecommerce.sb_ecom.Model.Category;

import java.util.List;


public interface CategoryService {

    CategoryResponse getAllCategories();
    CategoryDTO createCategory(CategoryDTO category);
    CategoryDTO updateCategory(Long categoryId, CategoryDTO category);
    CategoryDTO  deleteCategory(Long categoryId);
}
