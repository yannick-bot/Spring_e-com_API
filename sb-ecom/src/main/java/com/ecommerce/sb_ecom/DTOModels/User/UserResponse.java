package com.ecommerce.sb_ecom.DTOModels.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private List<UserDTO> content;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer totalPages;
    private Integer totalElements;
    private boolean lastPage;

}
