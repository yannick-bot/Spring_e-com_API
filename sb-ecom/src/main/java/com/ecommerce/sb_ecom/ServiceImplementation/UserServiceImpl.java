package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.Repositories.UserRepository;
import com.ecommerce.sb_ecom.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
}
