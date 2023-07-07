package com.example.video_rental.users;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountServiceTest {
    @InjectMocks
    private AccountService accountService;

    private AutoCloseable autoCloseable;

    @Mock
    private AccountRepository mockAccountRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        accountService = new AccountService(mockAccountRepository);

        Account account = new Account(
                1L,
                "aster",
                "opensesame",
                null,
                Account.ACCOUNT_TYPE.Standard
        );

        when(mockAccountRepository.getAccountByUsername(account.username)).thenReturn(Optional.of(account));
        when(mockAccountRepository.save(account)).thenReturn(account);
    }

    /*-------------------------LOGIN-------------------------*/

    @Test
    void loginShouldReturnUser() {
        //When
        var u = accountService.login("aster", "opensesame");

        //Then
        assertInstanceOf(Account.class, u);
    }

    @Test
    void loginShouldGenerateTokenAndSetTokenOnAccount() {
        var u = accountService.login("aster", "opensesame");
        assertInstanceOf(String.class, u.token);
    }

    @Test
    void loginShouldThrowExceptionWithNoAccount() {

        //Then
        assertThrows(AccountException.InvalidLoginException.class, () -> accountService.login("jake", "opensesame"));
    }

    @Test
    void loginShouldThrowExceptionWithMismatchedPasswords() {
        assertThrows(AccountException.InvalidLoginException.class, () -> accountService.login("aster", "blah"));
    }

    /*-------------------------REGISTER-------------------------*/

    @Test
    void registerShouldThrowExceptionIfUserIsPresent() {
        Exception exception = assertThrows(AccountException.InvalidLoginException.class, () -> accountService.register(
                "aster", "opensesame"));
        assertEquals("Username is already taken", exception);
    }

    @Test
    void registerShouldSaveNewAccountToRepository() {
        Account addedAccount = accountService.register("jake", "opensesame");
        Account newAccount = new Account(1L, "jake", "opensesame", null, Account.ACCOUNT_TYPE.Standard);
        assertEquals(addedAccount.username, newAccount.username);
    }

    @Test
    void registerShouldUpdateToken() {
        Account u = accountService.register("jake", "opensesame");
        assertInstanceOf(String.class, u.token);
    }

    /*-------------------------REGISTER-------------------------*/

    


    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
}