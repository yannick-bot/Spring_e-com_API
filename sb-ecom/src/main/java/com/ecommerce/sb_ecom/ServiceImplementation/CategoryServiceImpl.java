package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.Model.Category;
import com.ecommerce.sb_ecom.Service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private List<Category> categories = new ArrayList<>();
    private static long id = 1;

    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public String  createCategory(Category category) {
        if(category.getCategoryName() == null || category.getCategoryName().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Some properties are missing");
        }
        category.setCategoryId(id++);
        categories.add(category);
        return "category added successsfully";
    }

    @Override
    public String updateCategory(Long categoryId, Category category) {
        Category categorySearched = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));

        categorySearched.setCategoryName(category.getCategoryName());
        return "category updated";
    }

    @Override
    public String  deleteCategory(Long categoryId) {


        Category category = categories.stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst()
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
        
            categories.remove(category);
        return "category deleted";
    }

}
