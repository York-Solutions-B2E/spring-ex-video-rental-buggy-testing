package com.example.video_rental.users;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class AccountController
{

    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService)
    {
        this.accountService = accountService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Account register(@RequestBody Account u)
    {
        return this.accountService.register(u.username,u.password);
    }
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Account> getAll(@RequestParam String token)
    {
        return this.accountService.getAllUsers(token);
    }


    @PostMapping("/login")
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Account login(@RequestBody Account u)
    {
        return this.accountService.login(u.username,u.password);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public Account me(@RequestParam String token)
    {
        return this.accountService.getUserFromToken(token);
    }
}
