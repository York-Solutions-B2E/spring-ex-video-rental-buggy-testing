package com.example.video_rental.rentals;


import com.example.video_rental.users.Account;
import com.example.video_rental.videos.Video;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Rental
{
    public enum RETURN_STATUS
    {
        ACTIVE,
        OVERDUE,
        RETURNED
    }

    public Rental()
    {

    }

    @Override
    public String toString()
    {
        return "Rental{" +
                "id=" + id +
                ", video=" + video +
                ", user=" + account +
                ", startDate=" + startDate +
                ", dueDate=" + dueDate +
                ", returnDate=" + returnDate +
                ", status=" + status +
                '}';
    }

    public Rental(Long id, Video video, Account account, Date startDate, Date dueDate)
    {
        this.id = id;
        this.video = video;
        this.account = account;
        this.dueDate = dueDate;
        this.startDate = startDate;
        this.returnDate = null;

    }

    @PostLoad
    public void fillStatus()
    {
        if(this.returnDate != null)
        {
            this.status = RETURN_STATUS.RETURNED;
        }
        else if(new Date().before(this.dueDate))
        {
            this.status = RETURN_STATUS.ACTIVE;
        }
        else
        {
            this.status = RETURN_STATUS.OVERDUE;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;


    @JoinColumn(nullable = false)
    @ManyToOne(cascade = CascadeType.PERSIST)
    public Video video;



    @ManyToOne(cascade = CascadeType.ALL)
    @JsonProperty("RentalUser")
    public Account account;

    @JsonGetter("RentalUser")
    public String getUserName()
    {
        return account.username;
    }
    @JsonSetter("RentalUser")
    public void setUser(Account u)
    {
        this.account = u;
    }

    @Column(nullable = false)
    public Date startDate;

    @Column(nullable = false)
    public Date dueDate;
    public Date returnDate;


    @Transient
    public RETURN_STATUS status;

}
