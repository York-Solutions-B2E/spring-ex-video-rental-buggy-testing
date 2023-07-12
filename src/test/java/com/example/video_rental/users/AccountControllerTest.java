package com.example.video_rental.users;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

import org.hamcrest.collection.*;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(controllers = AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@EnableWebMvc
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private Account testAccount;

    @BeforeEach
    public void setup() {
        testAccount = new Account();
        testAccount.accountType = Account.ACCOUNT_TYPE.Standard;
        testAccount.password = "1234";
        testAccount.id = 3L;
        testAccount.rentals = null;
        testAccount.username = "RubberDuck";
        testAccount.token = "abcd";
    }

    @Test
    public void test()
    {
        assertTrue(true);
    }

    // Practice
    // In this test I practiced using a get method.
    // Deconstructed the nested methods used in testing http requests to understand their types
    @Test
    public void testGetMethodPractice() throws Exception
    {
        List<Account> accounts = List.of(this.testAccount);

        when(accountService.getAllUsers(anyString())).thenReturn(accounts);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .get("/user/users")
                .param("token", "abcd")
                .contentType(MediaType.APPLICATION_JSON);

        ResultActions response = mockMvc.perform(mockRequest);

        MvcResult result = response.andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    // Integration
        // Tests to make sure the endpoint performs a successful get request when the token is defined and doesn't when null
    @Test
    public void usersEndpointShouldValidateTokenParameter() throws Exception
    {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                .get("/user/users")
                .param("token", "abcd"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        MvcResult resultNullToken = this.mockMvc.perform(MockMvcRequestBuilders
                     .get("/user/users"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }


    // Integration
        // Tests that the endpoint returns formatted JSON data strings for scenarios with and without users
    @Test
    public void usersEndpointShouldReturnProperStringData() throws Exception
    {
        MvcResult emptyResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/users")
                        .param("token", "abcd"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        var stringResult = emptyResult.getResponse().getContentAsString();
        assertEquals(stringResult, "[]");

        List<Account> accounts = List.of(testAccount);

        when(accountService.getAllUsers(any())).thenReturn(accounts);

        MvcResult populatedResponse = this.mockMvc.perform(MockMvcRequestBuilders
                    .get("/user/users")
                    .param("token", "abcd"))
            .andExpect(MockMvcResultMatchers.status().is(200))
            .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String expected = "[" + mapper.writeValueAsString(testAccount) + "]";
        String actual = populatedResponse.getResponse().getContentAsString();

        assertEquals(actual, expected);

    }

    // Integration
        // 1. Confirms that a properly formatted request body in a POST request results in a 201
        // 2. Confirms that lacking a request body in a POST request will result in a 400
    @Test
    public void registerEndpointShouldValidateRequestBody() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testAccount)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();
        MvcResult resultEmptyUser = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();
    }

    // Integration
        // Tests that the object received in the JSON from a POST request is formatted properly
    @Test
    public void registerEndpointShouldReturnProperStringData() throws Exception
    {
        when(accountService.register(any(), any())).thenReturn(testAccount);

        ObjectMapper mapper = new ObjectMapper();

        MvcResult populatedResponse = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testAccount)))
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andExpect(jsonPath("$.accountType", is(Account.ACCOUNT_TYPE.Standard.name())))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.rentals", is(testAccount.rentals)))
                .andExpect(jsonPath("$.username", is(testAccount.username)))
                .andExpect(jsonPath("$.token", is(testAccount.token)))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn();
    }

    // Integration
    // 1. Confirms that a properly formatted request body in a POST request results in a 201
    // 2. Confirms that lacking a request body in a POST request will result in a 400
    @Test
    public void loginEndpointShouldValidateRequestBody() throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testAccount)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        MvcResult resultEmptyUser = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();
    }

    // Integration
    // Tests that the object received in the JSON from a POST request is formatted properly
    @Test
    public void loginEndpointShouldReturnProperStringData() throws Exception
    {
        when(accountService.login(any(), any())).thenReturn(testAccount);

        ObjectMapper mapper = new ObjectMapper();

        MvcResult populatedResponse = this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(testAccount)))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(jsonPath("$.accountType", is(Account.ACCOUNT_TYPE.Standard.name())))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.rentals", is(testAccount.rentals)))
                .andExpect(jsonPath("$.username", is(testAccount.username)))
                .andExpect(jsonPath("$.token", is(testAccount.token)))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andReturn();
    }

    // Integration
    // Tests to make sure the endpoint performs a successful get request when the token is defined and doesn't when null
    @Test
    public void meEndpointShouldValidateTokenParameter() throws Exception
    {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/me")
                        .param("token", "abcd"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        MvcResult resultNullToken = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/me"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();
    }

    // Integration
    // 1. Tests that the endpoint returns an empty JSON object when there is not a user
    // 2. Tests that the endpoint returns properly formatted JSON data strings when there is a user
    @Test
    public void meEndpointShouldReturnProperStringData() throws Exception
    {
        MvcResult emptyResult = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/me")
                        .param("token", "abcd"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        var stringEmptyResult = emptyResult.getResponse().getContentAsString();

        assertEquals("", stringEmptyResult);

        when(accountService.getUserFromToken(any())).thenReturn(testAccount);

        MvcResult populatedResponse = this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/user/me")
                        .param("token", "abcd"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        String expected = mapper.writeValueAsString(testAccount);
        String actual = populatedResponse.getResponse().getContentAsString();

        assertEquals(expected, actual);
    }

    // Unit test
    // Tests that register calls accountService.register method once
    @Test
    public void registerShouldCallServiceAndReturnExpectedUser()
    {
        when(accountService.register(any(), any())).thenReturn(testAccount);

        Account result = accountController.register(testAccount);

        verify(accountService, times(1)).register(any(), any());
        assertEquals(testAccount, result);
    }

    // Unit test
    // Tests that getAll calls accountService.getAllUsers once and returns Iterable with users
    @Test
    public void getAllShouldCallServiceAndReturnIterableOfExpectedUsers()
    {
        List<Account> accounts = List.of(testAccount);

        when(accountService.getAllUsers(any())).thenReturn(accounts);

        Iterable<Account> result = accountController.getAll("token");

        verify(accountService, times(1)).getAllUsers(any());
        assertEquals(accounts, result);
    }

    // Unit test
    // Tests that login calls accountService.getAllUsers once and returns logged-in user
    @Test
    public void loginShouldCallServiceAndReturnIterableOfExpectedUsers()
    {
        when(accountService.login(any(), any())).thenReturn(testAccount);

        Account result = accountController.login(testAccount);

        verify(accountService, times(1)).login(any(), any());
        assertEquals(testAccount, result);
    }

    @Test
    public void meShouldCallServiceAndReturnExpectedUser()
    {
        when(accountService.getUserFromToken(any())).thenReturn(testAccount);

        Account result = accountController.me("token");

        verify(accountService, times(1)).getUserFromToken(any());
        assertEquals(testAccount, result);
    }


}