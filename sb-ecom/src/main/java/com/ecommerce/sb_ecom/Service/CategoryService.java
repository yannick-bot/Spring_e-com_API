package com.ecommerce.sb_ecom.Service;

import com.ecommerce.sb_ecom.Model.Category;

import java.util.List;


public interface CategoryService {

    List<Category> getAllCategories();
    String createCategory(Category category);
    String updateCategory(Long categoryId, Category category);
    String  deleteCategory(Long categoryId);
}
