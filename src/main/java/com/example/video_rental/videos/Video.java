package com.example.video_rental.videos;

import com.example.video_rental.rentals.Rental;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(title, video.title) && Objects.equals(image, video.image) && Objects.equals(genres, video.genres) && Objects.equals(copies, video.copies) && Objects.equals(rentals, video.rentals);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, image, genres, copies, rentals);
    }
}
