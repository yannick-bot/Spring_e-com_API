package com.ecommerce.sb_ecom.DTOModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
   private List<CategoryDTO> content;
   private Integer pageNumber;
   private Integer pageSize;
   private Integer totalElements;
   private Integer totalPages;
   private boolean lastPage;

}
