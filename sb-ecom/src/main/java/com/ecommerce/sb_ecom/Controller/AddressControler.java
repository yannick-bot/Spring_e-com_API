package com.ecommerce.sb_ecom.Controller;

import com.ecommerce.sb_ecom.Config.AppConstants;
import com.ecommerce.sb_ecom.DTOModels.Address.AddressDTO;
import com.ecommerce.sb_ecom.DTOModels.Address.AddressResponse;
import com.ecommerce.sb_ecom.Service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class AddressControler {
    @Autowired
    private AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody AddressDTO addressDTO, Authentication authentication) {
        return new ResponseEntity<>(addressService.createAddress(addressDTO, authentication.getName()), HttpStatus.CREATED);
    }


    @GetMapping("/addresses")
    public ResponseEntity<AddressResponse> getAllAddresses(@RequestParam(defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
                                                           @RequestParam(defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
                                                           @RequestParam(defaultValue = AppConstants.SORT_ADDRESSES_BY, required = false) String sortBy,
                                                           @RequestParam(defaultValue = AppConstants.SORT_ORDER, required = false) String sortOrder) {
        return new ResponseEntity<>(addressService.getAllAddresses(pageNumber, pageSize, sortOrder, sortBy), HttpStatus.OK);
    }


    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        return new ResponseEntity<>(addressService.getAddressById(addressId), HttpStatus.OK);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getUserAddress(Authentication authentication) {
        return new ResponseEntity<>(addressService.getUserAddress(authentication.getName()), HttpStatus.OK);
    }


    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId, @RequestBody AddressDTO addressDTO) {
        return new ResponseEntity<>(addressService.updateAddress(addressId, addressDTO), HttpStatus.OK);
    }


    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId, Authentication authentication) {
        return new ResponseEntity<>(addressService.deleteAddress(addressId, authentication.getName()), HttpStatus.OK);
    }
}
