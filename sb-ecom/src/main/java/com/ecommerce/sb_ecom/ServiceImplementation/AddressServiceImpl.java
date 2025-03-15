package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.Repositories.AddressRepository;
import com.ecommerce.sb_ecom.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;
}
