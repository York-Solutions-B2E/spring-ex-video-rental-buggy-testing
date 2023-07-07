package com.example.video_rental.users;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
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
        assertInstanceOf(Account.class, u, "should return user that logged in");
    }

    @Test
    void loginShouldGenerateTokenAndSetTokenOnAccount() {
        var u = accountService.login("aster", "opensesame");
        assertInstanceOf(String.class, u.token, "should be a string token");
    }

    @Test
    void loginShouldUpdateUserTokenMap() {
        final Map<String, Long> userTokenMap = new HashMap<>();

        var u = accountService.login("aster", "opensesame");
        Object uMap = new HashMap<>().put("aster", 1L);

        assertEquals(userTokenMap.get("aster"), uMap, "should return objects matching 'aster' and 1L");
    }

    @Test
    void loginShouldThrowExceptionWithNoAccount() {

        //Then
        assertThrows(AccountException.InvalidLoginException.class, () -> accountService.login("jake", "opensesame"),
                     "should throw invalidloginexception class from wrong username");
    }

    @Test
    void loginShouldThrowExceptionWithMismatchedPasswords() {
        assertThrows(AccountException.InvalidLoginException.class, () -> accountService.login("aster", "blah"),
                     "should throw invalidloginexception class from wrong password");
    }

    /*-------------------------REGISTER-------------------------*/

    @Test
    void registerShouldThrowExceptionIfUserIsPresent() {
        assertThrows(AccountException.InvalidLoginException.class, () -> accountService.register(
                "aster", "opensesame"));
    }

    @Test
    void registerShouldSaveNewAccountToRepository() {
        Account addedAccount = accountService.register("jake", "opensesame");
        Account newAccount = new Account(1L, "jake", "opensesame", null, Account.ACCOUNT_TYPE.Standard);
        assertEquals(addedAccount.username, newAccount.username, "should match added account and new account");
    }

    @Test
    void registerShouldUpdateToken() {
        Account u = accountService.register("jake", "opensesame");
        assertInstanceOf(String.class, u.token, "should return a string");
    }

    /*-------------------------getUserFromToken-------------------------*/

    @Test
    void getUserFromTokenShouldReturnMatchedAccount() {
    }


    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
}