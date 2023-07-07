package com.example.video_rental.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends CrudRepository<Account, Long>
{
    // should be "String password" not "String username"
    Optional<Account> getAccountByUsername(String username);
}
