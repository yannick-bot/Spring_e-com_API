package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.DTOModels.Category.CategoryDTO;
import com.ecommerce.sb_ecom.DTOModels.Category.CategoryResponse;
import com.ecommerce.sb_ecom.Exceptions.ApiException;
import com.ecommerce.sb_ecom.Exceptions.RessourceNotFoundException;
import com.ecommerce.sb_ecom.Model.Category;
import com.ecommerce.sb_ecom.Repositories.CategoryRepository;
import com.ecommerce.sb_ecom.Service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        //sorting
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //pagination
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);


        // first verifying the category list isn't empty
        List<Category> categories = categoryPage.getContent();
        if(categories.isEmpty()) {
            throw new ApiException("No category created till now");
        }

        // ...if not, converting the list of Category into CategoryDTO
        List<CategoryDTO> categoryDTOS = categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements((int) categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());

        return categoryResponse;
    }

    @Override
    public CategoryDTO  createCategory(CategoryDTO categoryDTO) {

        // mapping the CategoryDTO to the Category model
        Category category = modelMapper.map(categoryDTO, Category.class);

        // using the mapped model for the business logic
        Optional<Category> optionalCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(optionalCategory.isPresent()) {
            throw new ApiException(String.format("Category already exists: %s", category.getCategoryName()));
        }
        Category savedCategory = categoryRepository.save(category);

        // ...then re-map to the CategoryDTO for sending to the client
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        Category categorySaved = categoryOptional
                .orElseThrow( () -> new RessourceNotFoundException("Category", "CategoryId", categoryId));


        categorySaved.setCategoryName(categoryDTO.getCategoryName());
        Category updated = categoryRepository.save(categorySaved);
        CategoryDTO updatedDTO = modelMapper.map(updated, CategoryDTO.class);
        return updatedDTO;
    }

    @Override
    public CategoryDTO  deleteCategory(Long categoryId) {

        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);

        Category category = categoryOptional
                .orElseThrow( ()->  new RessourceNotFoundException("Category", "CategoryId", categoryId));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO getOneCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new RessourceNotFoundException("category", "categoryId", categoryId));

        return  modelMapper.map(category, CategoryDTO.class);
    }

}
