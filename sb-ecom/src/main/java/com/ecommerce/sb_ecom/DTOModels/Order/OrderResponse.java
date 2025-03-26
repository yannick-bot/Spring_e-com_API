package com.ecommerce.sb_ecom.DTOModels.Order;

import java.util.List;

public class OrderResponse {
    private List<OrderDTO> content;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer totalPages;
    private Integer totalElements;
    private boolean lastPage;
}
