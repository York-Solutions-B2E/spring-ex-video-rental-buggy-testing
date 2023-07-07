package com.example.video_rental.rentals;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RentalException extends RuntimeException
{
    public RentalException()
    {
    }

    public RentalException(String message)
    {
        super(message);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT,reason = "Account at maximum rentals")
    static class MaximumRentalException extends RentalException
    {
        public MaximumRentalException()
        {

        }

        public MaximumRentalException(String message)
        {
            super(message);
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Could not find desired rental")
    static class RentalNotFoundException extends RentalException
    {
        public RentalNotFoundException()
        {
        }

        public RentalNotFoundException(String message)
        {
            super(message);
        }
    }
}
