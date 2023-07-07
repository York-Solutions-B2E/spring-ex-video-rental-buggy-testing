package com.example.video_rental.videos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VideoRepositoryTest {

    @Autowired
    private VideoRepository underTest;

    private Video video1, video2;
    private Video savedVideo1, savedVideo2;

    @BeforeEach
    void setUp() {

        Set<String> genres1 = Set.of("Action", "Fantasy", "Drama");
        video1 = new Video();

        video1.title = "The Lord of the Rings: The Fellowship of the Ring";
        video1.image = "https://lotr.com/fotr.jpg";
        video1.genres = genres1;
        video1.copies = 10;

        savedVideo1 = underTest.save(video1);

        Set<String> genres2 = Set.of("Action", "Sci-Fi");
        video2 = new Video();

        video2.title = "The Matrix";
        video2.image = "https://thematrix.com/matrix.jpg";
        video2.genres = genres2;
        video2.copies = 5;

        savedVideo2 = underTest.save(video2);
    }

    @AfterEach
    void tearDown() {
        underTest.deleteAll();
    }

    @Test
    void itShouldGetActionVideosForAction() {
        // given
        Iterable<Video> action = List.of(savedVideo1);

        // when
        Iterable<Video> expected = underTest.findAllByGenre("action");

        // then
        assertThat(expected).containsExactlyInAnyOrderElementsOf(action);
    }




}