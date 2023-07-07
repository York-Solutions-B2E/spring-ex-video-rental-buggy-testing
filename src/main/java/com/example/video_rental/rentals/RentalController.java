package com.example.video_rental.rentals;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rental")
public class RentalController
{

    private RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService)
    {
        this.rentalService = rentalService;
    }

    @PostMapping("/request/{videoID}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Rental requestRental(@RequestParam("token") String token, @PathVariable("videoID") Long id, @RequestParam(value = "days",defaultValue = "7") Integer days)
    {
        if(token.isEmpty() || id <= 0)
        {
            return null;
        }
        return this.rentalService.requestRental(token,id,days);
    }
    @PutMapping("/return")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Rental returnRental(@RequestParam("token") String token, @RequestParam(value = "rentalID",required = true) Long id)
    {
        return this.rentalService.returnRental(token,id);
    }

}
