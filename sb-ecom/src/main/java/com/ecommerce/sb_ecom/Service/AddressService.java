package com.ecommerce.sb_ecom.Service;

import com.ecommerce.sb_ecom.DTOModels.Address.AddressDTO;
import com.ecommerce.sb_ecom.DTOModels.Address.AddressResponse;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, String name);

    AddressResponse getAllAddresses(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy);

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getUserAddress(String name);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId, String name);
}
