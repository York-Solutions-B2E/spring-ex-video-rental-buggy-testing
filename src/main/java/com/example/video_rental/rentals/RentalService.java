package com.example.video_rental.rentals;

import com.example.video_rental.users.Account;
import com.example.video_rental.users.AccountService;
import com.example.video_rental.videos.Video;
import com.example.video_rental.videos.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class RentalService
{
    private static final Integer maximumRentals = 3;
    private RentalRepository repo;
    private VideoService videoService;
    private AccountService accountService;

    @Autowired
    public RentalService(RentalRepository repo, VideoService videoService, AccountService accountService)
    {
        this.repo = repo;
        this.videoService = videoService;
        this.accountService = accountService;
    }

    public Rental requestRental(String token, Long videoID, Integer days)
    {
        Account account = accountService.getUserFromToken(token);
        if(account.rentals.size() >= maximumRentals)
        {
            throw new RentalException.MaximumRentalException("User trying to make new rental when at maximum");
        }
        Video video = videoService.getAvailableVideo(videoID);
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR,7);
        Rental rental = new Rental(null,video, account, new Date(), cal.getTime());
        rental.video = video;
        rental.account = account;
        account.rentals.add(rental);
        video.rentals.add(rental);

        this.repo.save(rental);
        return rental;
    }
    public Rental returnRental(String token, Long rentalID)
    {
        Account account = accountService.getUserFromToken(token);
        Optional<Rental> corresponding = account.rentals.stream().filter( (Rental r) -> {
            return r.id == rentalID;
        }).findAny();

        if(corresponding.isEmpty())
        {
            throw new RentalException.RentalNotFoundException("Rental with given ID could not be found");
        }
        Rental r = corresponding.get();
        r.returnDate = new Date();
        r.fillStatus();
        this.repo.save(r);
        return r;
    }
}
