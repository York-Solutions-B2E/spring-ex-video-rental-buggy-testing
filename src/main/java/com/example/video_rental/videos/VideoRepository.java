package com.example.video_rental.videos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface VideoRepository extends CrudRepository<Video,Long>
{
//    @Query("SELECT v FROM Video v JOIN v.genres g WHERE g=:genres")
//    Iterable<Video> findAllByGenre(@Param("genre") String genre);

        @Query("SELECT v FROM Video v JOIN v.genres g WHERE g=:genre")
        Iterable<Video> findAllByGenre(@Param("genre") String genre);


}
