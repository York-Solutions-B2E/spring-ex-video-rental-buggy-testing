package com.example.video_rental.users;

import com.example.video_rental.rentals.Rental;
import com.example.video_rental.users.Account;
import com.example.video_rental.videos.Video;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.runtime.ObjectMethods;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;


// Pulled from Max's example.

// Questions:

// 1. is testJacksonDeserialization a unit test?

// no, it's testing a jackson library (to make sure it works with our implementation)

// 2. What parts of the Account class are not tested in this code?

//    the imports and fields (and annotations)

// Answers:

// Read this after answering those:
// the serialization and deserialization methods are not testing a unit,
// they are testing how something seperate (jackson) interacts with our entity.
// On top of that it is indirectly testing the serialization of other classes.
// This is what would be called an integration test, and we will be writing plenty of these as well.

// There is no in-code distinction between an integration and unit test,
// it is up to how we write them and how we define a 'unit'.

// We are not testing the entity related annotations (OneToMany, Transient, etc.)
// - and we should write integration tests to make sure those annotations
// are being followed properly where they would be used

// Make sure your tests are accounting for this,
// and write comments describing your test as a unit test or integration test

public class AccountTest {

    private Account account;

    @BeforeEach
    public void setup() {
        this.account = null;
    }

    @Test
    public void testNoArgsConstructor() {
        account = new Account();
        assertEquals(new ArrayList<>(), account.rentals, "Rentals should be initialized with an empty list");
    }

    @Test
    public void testAllArgsConstructor() {
        List<Rental> rentals = new ArrayList<>();
        rentals.add(new Rental());
        account = new Account(1L, "user1", "pass1", rentals, Account.ACCOUNT_TYPE.Standard);

        assertEquals(1L, account.id, "Id should be 1");
        assertEquals("user1", account.username, "Username should be user1");
        assertEquals("pass1", account.password, "Password should be pass1");
        assertEquals(rentals, account.rentals, "Rentals should be equal to initialized value");
        assertEquals(Account.ACCOUNT_TYPE.Standard, account.accountType, "Account type should be Standard");

        account = new Account(1L, "user1", "pass1", rentals, Account.ACCOUNT_TYPE.Admin);
        assertEquals(1L, account.id, "Id should be 1");
        assertEquals("user1", account.username, "Username should be user1");
        assertEquals("pass1", account.password, "Password should be pass1");
        assertEquals(rentals, account.rentals, "Rentals should be equal to initialized value");
        assertEquals(Account.ACCOUNT_TYPE.Admin, account.accountType, "Account type should be Admin");
    }

    @Test
    public void testJacksonSerialization() throws Exception
    {
        // given
        // Making a list of one rental to give to new account
        List<Rental> rentals = new ArrayList<>();
        Rental r = new Rental();
        Video v = new Video();
        v.id = 1L;
        v.rentals.add(r);
        v.genres = new HashSet<>();
        v.genres.add("genre1");
        v.genres.add("genre2");
        v.image = "image";
        v.copies = 3;
        v.title = "title";
        r.video = v;
        rentals.add(r);

        // can pass in rentals here and edit later because it's a reference variable
        this.account = new Account(1L,"user1","pass1",rentals, Account.ACCOUNT_TYPE.Standard);
        this.account.token = "token";
        r.account = this.account;
        r.id = 2L;
        r.startDate = new Date(100,0,1);
        r.returnDate = new Date(100,0,2);
        r.dueDate = new Date(100,0,3);
        r.status = Rental.RETURN_STATUS.RETURNED;

        // comes from jackson
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this.account);

        assertEquals(json,"{\"id\":1,\"accountType\":\"Standard\",\"username\":\"user1\",\"token\":\"token\",\"rentals\":[{\"id\":2,\"video\":{\"id\":1,\"title\":\"title\",\"image\":\"image\",\"genres\":[\"genre1\",\"genre2\"],\"copies\":3,\"available\":true},\"startDate\":946706400000,\"dueDate\":946879200000,\"returnDate\":946792800000,\"status\":\"RETURNED\",\"RentalUser\":\"user1\"}]}","JSON string produced should match given (note: no password should be present)");
    }

    @Test
    public void TestJacksonDeserialization() throws Exception
    {
        String json = "{\"id\":1, \"password\":\"pass1\",  \"accountType\":\"Standard\",\"username\":\"user1\",\"token\":\"token\"}";
        ObjectMapper mapper = new ObjectMapper();

        Account acc = mapper.readValue(json,Account.class);

        assertEquals(acc.id,1L,"ID should be 1");
        assertEquals(acc.username,"user1","Username should be user1");
        assertEquals(acc.password,"pass1","Password should be pass1");
        assertEquals(acc.token,"token","Token should be token");
        assertEquals(acc.accountType, Account.ACCOUNT_TYPE.Standard,"Account Type should be standard");
        assertEquals(acc.rentals, new ArrayList<>() ,"Rentals should be empty list");

    }
}