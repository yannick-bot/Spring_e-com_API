package com.ecommerce.sb_ecom.DTOModels.Address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private Long addressId;
    private String building_name;
    private String city;
    private String country;
    private String pincode;
    private String state;
    private String street;
}
