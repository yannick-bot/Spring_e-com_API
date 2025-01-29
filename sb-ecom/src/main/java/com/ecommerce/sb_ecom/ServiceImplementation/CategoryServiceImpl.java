package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.Model.Category;
import com.ecommerce.sb_ecom.Repositories.CategoryRepository;
import com.ecommerce.sb_ecom.Service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;


    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public String  createCategory(Category category) {
        categoryRepository.save(category);
        return "category added successsfully";
    }

    @Override
    public String updateCategory(Long categoryId, Category category) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        Category categorySaved = categoryOptional
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));


        categorySaved.setCategoryName(category.getCategoryName());
        categoryRepository.save(categorySaved);
        return "category updated";
    }

    @Override
    public String  deleteCategory(Long categoryId) {

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        Category category = categoryOptional
                .orElseThrow( ()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        categoryRepository.delete(category);
        return "category deleted";
    }

}
