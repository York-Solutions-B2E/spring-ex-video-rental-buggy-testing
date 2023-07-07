package com.example.video_rental.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

class AccountException extends RuntimeException
{

    public AccountException(String message)
    {
        super(message);
    }

    public AccountException()
    {
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid Login/Registration: Bad username/password or username already taken")
    static class InvalidLoginException extends RuntimeException
    {
        public InvalidLoginException(String message)
        {
            super(message);
        }

        public InvalidLoginException() {
            super();
        }
    }
    @ResponseStatus(value = HttpStatus.OK, reason = "You are not authorized to make that request")
    static class UnauthorizedException extends RuntimeException
    {
        public UnauthorizedException(String message) {
            super(message);
        }

        public UnauthorizedException() {
            super();
        }
    }

    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    static class BadTokenException extends RuntimeException
    {
        public BadTokenException()
        {
        }

        public BadTokenException(String message)
        {
            super(message);
        }
    }
}
