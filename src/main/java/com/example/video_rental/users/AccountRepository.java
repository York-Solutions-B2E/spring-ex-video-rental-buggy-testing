package com.example.video_rental.users;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AccountRepository extends CrudRepository<Account, Long>
{
    Optional<Account> getUserByPassword(String username);
}
