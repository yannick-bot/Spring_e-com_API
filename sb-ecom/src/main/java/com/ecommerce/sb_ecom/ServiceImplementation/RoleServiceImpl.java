package com.ecommerce.sb_ecom.ServiceImplementation;

import com.ecommerce.sb_ecom.Repositories.RoleRepository;
import com.ecommerce.sb_ecom.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
}
