package com.example.video_rental.users;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository underTest;
    @Test
    void itShouldSelectUserByPassword() {
        Account account = new Account(
                1L,
                "aster",
                "opensesame",
                null,
                Account.ACCOUNT_TYPE.Standard
        );
        underTest.save(account);

        //when
        boolean testAccount = underTest.getAccountByUsername("aster").isPresent();

        assertEquals(true, testAccount);

    }

}