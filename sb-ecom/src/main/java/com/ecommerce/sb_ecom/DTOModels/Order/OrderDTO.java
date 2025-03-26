package com.ecommerce.sb_ecom.DTOModels.Order;

import com.ecommerce.sb_ecom.DTOModels.OrderItem.OrderItemDTO;
import com.ecommerce.sb_ecom.DTOModels.Payment.PaymentDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long orderId;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private Date orderDate;
    @NotBlank
    private String orderStatus;
    private Double totalAmount;

    private PaymentDTO paymentDTO;

    private List<OrderItemDTO> orderItemDTOList;

    private Long addressId;
}
