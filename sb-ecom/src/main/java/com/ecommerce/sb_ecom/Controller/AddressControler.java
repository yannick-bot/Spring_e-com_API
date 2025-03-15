package com.ecommerce.sb_ecom.Controller;

import com.ecommerce.sb_ecom.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class AddressControler {
    @Autowired
    private AddressService addressService;
}
