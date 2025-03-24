package com.ecommerce.sb_ecom.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;
    @NotBlank
    @Size(min = 5, message = "Building name must be atleast 5 characters")
    private String building_name;
    @NotBlank
    @Size(min = 5, message = "City name must be atleast 5 characters")
    private String city;
    @NotBlank
    @Size(min = 2, message = "Country name must be atleast 2 characters")
    private String country;
    @NotBlank
    @Size(min = 6, message = "Pincode name must be atleast 6 characters")
    private String pincode;
    @NotBlank
    @Size(min = 2, message = "State name must be atleast 2 characters")
    private String state;
    @NotBlank
    @Size(min = 5, message = "Street name must be atleast 5 characters")
    private String street;

    @ToString.Exclude
    @ManyToMany(mappedBy = "addressList")
    private List<User> users = new ArrayList<>();

    public void setUsers(User user) {
        this.users.add(user);
        user.getAddressList().add(this);
    }

}
