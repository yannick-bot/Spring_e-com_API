package com.ecommerce.sb_ecom.DTOModels.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private List<ProductDTO> content;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer totalPages;
    private Integer totalElements;
    private boolean lastPage;
}
