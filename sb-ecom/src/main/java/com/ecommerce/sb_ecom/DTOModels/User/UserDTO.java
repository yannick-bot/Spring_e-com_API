package com.ecommerce.sb_ecom.DTOModels.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    @Size(min = 3)
    private String username;
    @Email
    private String email;
    @Size(min = 8, message = "password must be at least 8 characters length")
    private String password;
}
