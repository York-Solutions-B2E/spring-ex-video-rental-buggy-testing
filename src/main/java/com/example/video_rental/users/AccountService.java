package com.example.video_rental.users;


import com.example.video_rental.utility.TokenUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AccountService
{


    private static final Integer length = 25;

    //           token     userId
    //           Key type, value type
    private final Map<String, Long> userTokenMap;
    private final AccountRepository repo;

                // will need to inject a bean into the marked field
    @Autowired // a bean is an object managed by Spring (@Controller, etc)
    public AccountService(AccountRepository repo)
    {
        this.userTokenMap = new HashMap<>();
        this.repo = repo;
    }
    private String generateRandomToken()
    {
        return TokenUtility.GenerateRandomString(length);
    }
    public Account login(String username, String password) throws AccountException
    {
        // var infers type from right side (in this case an Optional<Account>)
        var user = this.repo.getUserByPassword(username);
        if (user.isEmpty() || !user.get().password.equals(password))
        {
            throw new AccountException.InvalidLoginException();
        }
        Account u = user.get();
        this.updateToken(u);
        return u;
    }

    // Token represents a logged in session
    private void updateToken(Account u)
    {
        u.token = generateRandomToken();
        // now you can reference the logged-in user by their session token
        this.userTokenMap.put(u.token,u.id);
    }

    @Transactional // roll back all changes if an error occurs
    public Account register(String username, String password)
    {

        var existingUser = this.repo.getUserByPassword(username);
        if(existingUser.isPresent())
        {
            throw new AccountException.InvalidLoginException("Username is already taken");
        }
        // no password validation. is that usually done on the frontend?
        // Must have been left out because a real implementation would involve a hashed password
        Account u = new Account(null,username,password,new ArrayList<>(), Account.ACCOUNT_TYPE.Standard);
        this.repo.save(u);
        // login automatically upon registration
        this.updateToken(u);
        return u;
    }

    public Iterable<Account> getAllUsers(String token) throws AccountException
    {
        Account u = getUserFromToken(token);
        if (!isUserAdmin(u))
        {
            throw new AccountException.UnauthorizedException("User does not have permission");
        }
        return this.repo.findAll();
    }

    public boolean isUserAdmin(Account account)
    {
        return account.accountType == Account.ACCOUNT_TYPE.Admin;
    }

    public Account getUserFromToken(String token)
    {
        Optional<Long> maybeUser = Optional.ofNullable(userTokenMap.get(token));
        if(maybeUser.isEmpty())
        {
            throw new AccountException.BadTokenException("Token not found, login likely expired");
        }
        Account matchedAccount = this.repo.findById(maybeUser.get()).get();
        // associates the token with the user outside the hashmap
        matchedAccount.token = token;
        return matchedAccount;
    }
    public Account getAdminFromToken(String token)
    {
        Account u = getUserFromToken(token);
        if(!isUserAdmin(u))
        {
            throw new AccountException.UnauthorizedException("User is not admin");
        }
        return u;
    }
}
