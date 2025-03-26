package com.ecommerce.sb_ecom.DTOModels.OrderItem;

import com.ecommerce.sb_ecom.DTOModels.Order.OrderDTO;
import com.ecommerce.sb_ecom.DTOModels.Product.ProductDTO;

public class OrderItemDTO {
    private Long orderItemId;
    private Double discount;
    private Double orderedProductPrice;
    private Integer quantity;

    private ProductDTO product;

    private OrderDTO order;
}
