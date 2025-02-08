package com.ecommerce.sb_ecom.DTOModels.Category;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryID;
    @Size(min = 5, message = "categoryName must contains at least 5 characters")
    private String categoryName;
}
