package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.DTOModels.Address.AddressDTO;
import com.ecommerce.sb_ecom.DTOModels.Address.AddressResponse;
import com.ecommerce.sb_ecom.Exceptions.ApiException;
import com.ecommerce.sb_ecom.Model.Address;
import com.ecommerce.sb_ecom.Model.User;
import com.ecommerce.sb_ecom.Repositories.AddressRepository;
import com.ecommerce.sb_ecom.Repositories.UserRepository;
import com.ecommerce.sb_ecom.Service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, String name) {
        Address address = modelMapper.map(addressDTO, Address.class);
        Optional<User> optUser = userRepository.findByUsername(name);
        if (optUser.isEmpty()) {
            throw new ApiException(String.format("User with username %s not found", name));
        }

        //gérer la relation bidirectionnel
        User user = optUser.get();
        address.setUsers(user);
        Address saved = addressRepository.save(address);
        userRepository.save(user);
        return modelMapper.map(saved, AddressDTO.class);
    }

    @Override
    public AddressResponse getAllAddresses(Integer pageNumber, Integer pageSize, String sortOrder, String sortBy) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable page = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Address> addressPage = addressRepository.findAll(page);
        List<Address> addressList = addressPage.getContent();

        if (addressList.isEmpty()) {
            throw new ApiException("Address repository is empty");
        }

        List<AddressDTO> addressDTOList = addressList.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();

        AddressResponse addressResponse = new AddressResponse();
        addressResponse.setContent(addressDTOList);
        addressResponse.setLastPage(addressPage.isLast());
        addressResponse.setPageNumber(addressPage.getNumber());
        addressResponse.setPageSize(addressPage.getSize());
        addressResponse.setTotalPages(addressPage.getTotalPages());
        addressResponse.setTotalElements(addressPage.getNumberOfElements());

        return addressResponse;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {

        Optional<Address> optional = addressRepository.findById(addressId);
        if (optional.isEmpty()) {
            throw new ApiException("Address not found");
        }
        Address address = optional.get();

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getUserAddress(String name) {
        Optional<User> optUser = userRepository.findByUsername(name);
        if (optUser.isEmpty()) {
            throw new ApiException(String.format("User with username %s not found", name));
        }

        User user = optUser.get();
        List<Address> addresses = addressRepository.findAllByUser(user);
        if (addresses.isEmpty()) {
            throw new ApiException("USER has no address");
        }

        List<AddressDTO> addressDTOList = addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();

        return addressDTOList;
    }


    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) {
        Optional<Address> optional = addressRepository.findById(addressId);
        if (optional.isEmpty()) {
            throw new ApiException("Address not found");
        }
        Address address = optional.get();

        Address newAddress = modelMapper.map(addressDTO, Address.class);
        newAddress.setAddressId(address.getAddressId());


        return modelMapper.map(addressRepository.save(newAddress), AddressDTO.class);
    }


    @Override
    public String  deleteAddress(Long addressId, String name) {
        // trouver l'addresse
        Optional<Address> optional = addressRepository.findById(addressId);
        if (optional.isEmpty()) {
            throw new ApiException("Address not found");
        }
        Address address = optional.get();

        // trouver le user
        Optional<User> optUser = userRepository.findByUsername(name);
        if (optUser.isEmpty()) {
            throw new ApiException(String.format("User with username %s not found", name));
        }
        User user = optUser.get();

        // enlever l'addresse de la liste d'addresses du user
        user.getAddressList().remove(address);
        userRepository.save(user);


        addressRepository.delete(address);
        return "Address deleted";
    }
}
