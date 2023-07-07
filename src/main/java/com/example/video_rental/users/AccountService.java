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
    private final Map<String, Long> userTokenMap;
    private final AccountRepository repo;

    @Autowired
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
        var user = this.repo.getAccountByUsername(username);
        if (user.isEmpty() || !user.get().password.equals(password))
        {
            throw new AccountException.InvalidLoginException();
        }
        Account u = user.get();
        this.updateToken(u);
        return u;
    }
    private void updateToken(Account u)
    {
        u.token = generateRandomToken();
        this.userTokenMap.put(u.token,u.id);
    }

    @Transactional
    public Account register(String username, String password)
    {

        var existingUser = this.repo.getAccountByUsername(username);
        if(existingUser.isPresent())
        {
            throw new AccountException.InvalidLoginException("Username is already taken");
        }

        Account u = new Account(null,username,password,new ArrayList<>(), Account.ACCOUNT_TYPE.Standard);
        this.repo.save(u);
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
