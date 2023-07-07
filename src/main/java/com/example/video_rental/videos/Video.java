package com.example.video_rental.videos;

import com.example.video_rental.rentals.Rental;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    public String title;
    public String image;

    @ElementCollection
    public Set<String> genres;

    public Integer copies;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    public Set<Rental> rentals = new HashSet<>();

    public boolean isAvailable()
    {
        if (copies == null) {
            return false; // or true, depending on what makes sense for your application
        }
        return (copies - rentals.size()) > 0;
    }
}
