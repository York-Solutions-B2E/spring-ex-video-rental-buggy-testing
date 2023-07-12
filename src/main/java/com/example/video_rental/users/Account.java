package com.example.video_rental.users;

import com.example.video_rental.rentals.Rental;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity()
public class Account
{
    public enum ACCOUNT_TYPE {
        Standard,
        Admin
    }


    public Account() {
        rentals = new ArrayList<>();
    }

    public Account(Long id, String username, String password, List<Rental> rentals, ACCOUNT_TYPE accountType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rentals = rentals;
        this.accountType = accountType;
    }

    @Id
    public Long id;


    public ACCOUNT_TYPE accountType;
    public String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String password;
    @Transient
    public String token;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Rental> rentals;


}
