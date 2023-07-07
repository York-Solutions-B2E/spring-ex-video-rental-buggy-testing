package com.example.video_rental.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class AccountServiceTest
{
    @Mock
    private AccountRepository repo;

    @InjectMocks
    private AccountService service;

    @BeforeEach
    void setUp() throws Exception
    {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldLogInSuccessfully()
    {
        // given
        String username = "test";
        String password = "test";
        Account account = new Account(1L, username, password, null, Account.ACCOUNT_TYPE.Standard);

        // when
        when(repo.getUserByPassword(anyString())).thenReturn(Optional.of(account));

        Account result = service.login(username, password);

        // then
        assertEquals(account, result);
        verify(repo, times(1)).getUserByPassword(username);
    }

    // When testing in Java you may come across the 'Reflection API'.
    // The second method here uses that.
    // Don't be scared to get semi-familiar with reflection.
// line in question: throw new AccountException.InvalidLoginException();
    @Test
    void shouldFailLogInDueToInvalidCredentials() throws Exception {
        String username = "test";
        String password = "test";
        when(repo.getUserByPassword(anyString())).thenReturn(Optional.empty());

        // The exception we want is package private.  We use part of the Java Reflection API to get the class
        // Better practice might be to have the test in the same package, but this is good to know about

        // accesses the deep magic (Java's class loader) to get an instance of a class outside of its access
        // any class              .forName() is a static method of "Class" class - takes qualified name and finds, loads, initializes
        Class<?> invalidLoginExceptionClass = Class.forName("com.example.video_rental.users.AccountException$InvalidLoginException");

        Exception exception = assertThrows(Exception.class, () -> service.login(username, password));

        assertTrue(invalidLoginExceptionClass.isInstance(exception));
        verify(repo, times(1)).getUserByPassword(username);
    }

    /// End example code from max

    @Test
    void register() {
    }

    @Test
    void getAllUsers() {
    }

    @Test
    void isUserAdmin() {
    }

    @Test
    void getUserFromToken() {
    }

    @Test
    void getAdminFromToken() {
    }



}



