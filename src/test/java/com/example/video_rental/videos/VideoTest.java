package com.example.video_rental.videos;

import com.example.video_rental.rentals.Rental;
import com.example.video_rental.users.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class VideoTest {

    private Video videoUnderTest;

    @Autowired
    VideoRepository videoRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        videoRepository.deleteAll();
    }

    // Unit tests
    @Test
    void isAvailableIsTrueWhenThereAreFewerRentalsThanCopies() {
        // given
        videoUnderTest = new Video();

        videoUnderTest.id = 1L;
        Rental r = new Rental();
        videoUnderTest.rentals.add(r);
        videoUnderTest.genres = new HashSet<>();
        videoUnderTest.genres.add("genre1");
        videoUnderTest.genres.add("genre2");
        videoUnderTest.image = "image";
        videoUnderTest.copies = 3;
        videoUnderTest.title = "title";

        // when
        boolean expected = videoUnderTest.isAvailable();

        // then
        assertTrue(expected);
    }

    @Test
    void isAvailableIsFalseWhenNoExtraCopies() {
        // given
        videoUnderTest = new Video();

        videoUnderTest.id = 1L;
        for(int i = 0; i < 3; i++){
            videoUnderTest.rentals.add(new Rental());
        }
        videoUnderTest.genres = new HashSet<>();
        videoUnderTest.genres.add("genre1");
        videoUnderTest.genres.add("genre2");
        videoUnderTest.image = "image";
        videoUnderTest.copies = 3;
        videoUnderTest.title = "title";

        // when
        boolean expected = videoUnderTest.isAvailable();

        // then
        assertFalse(expected);
    }

    @Test
    void isAvailableIsFalseWhenCopiesIsNull() {
        // given
        videoUnderTest = new Video();

        videoUnderTest.id = 1L;
        for(int i = 0; i < 3; i++){
            videoUnderTest.rentals.add(new Rental());
        }
        videoUnderTest.genres = new HashSet<>();
        videoUnderTest.genres.add("genre1");
        videoUnderTest.genres.add("genre2");
        videoUnderTest.image = "image";
        videoUnderTest.copies = null;
        videoUnderTest.title = "title";

        // when
        boolean expected = videoUnderTest.isAvailable();

        // then
        assertFalse(expected);
    }

    // Integration tests

    @Test
    public void serializesAllFieldsCorrectly() throws Exception
    {
        videoUnderTest = new Video();
        videoUnderTest.id = 1L;
        videoUnderTest.title = "The Lord of the Rings: The Two Towers";
        videoUnderTest.image = "rohan.jpg";
        videoUnderTest.genres = Set.of("Action");
        videoUnderTest.copies = 3;

        String actualJson =
                "{"
                + "\"id\":1,"
                + "\"title\":\"The Lord of the Rings: The Two Towers\","
                + "\"image\":\"rohan.jpg\","
                + "\"genres\":[\"Action\"],"
                + "\"copies\":3,"
                + "\"available\":true"
                + "}";

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this.videoUnderTest);

        assertEquals(json,actualJson,"JSON string produced should match given");
    }

    @Test
    public void doesNotSerializeRentals() throws Exception
    {
        Rental r = new Rental();

        videoUnderTest = new Video();
        videoUnderTest.id = 1L;
        videoUnderTest.title = "The Lord of the Rings: The Two Towers";
        videoUnderTest.image = "rohan.jpg";
        videoUnderTest.genres = Set.of("Action");
        videoUnderTest.copies = 3;
        videoUnderTest.rentals = new HashSet<>();
        videoUnderTest.rentals.add(r);

        String actualJson =
                "{"
                        + "\"id\":1,"
                        + "\"title\":\"The Lord of the Rings: The Two Towers\","
                        + "\"image\":\"rohan.jpg\","
                        + "\"genres\":[\"Action\"],"
                        + "\"copies\":3,"
                        + "\"available\":true"
                        + "}";

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this.videoUnderTest);

        assertEquals(json,actualJson,"JSON string produced should match given");
    }

    @Test
    public void serializesNullValuesCorrectly() throws Exception
    {
        videoUnderTest = new Video();

        String actualJson =
                "{"
                + "\"id\":null,"
                + "\"title\":null,"
                + "\"image\":null,"
                + "\"genres\":null,"
                + "\"copies\":null,"
                + "\"available\":false"
                + "}";

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this.videoUnderTest);

        assertEquals(json,actualJson,"JSON string produced should match given");
    }

    @Test
    public void serializesWithEmptyGenres() throws Exception
    {
        videoUnderTest = new Video();
        videoUnderTest.id = 1L;
        videoUnderTest.title = "The Lord of the Rings: The Two Towers";
        videoUnderTest.image = "rohan.jpg";
        videoUnderTest.genres = Set.of();
        videoUnderTest.copies = 3;

        String actualJson =
                "{"
                        + "\"id\":1,"
                        + "\"title\":\"The Lord of the Rings: The Two Towers\","
                        + "\"image\":\"rohan.jpg\","
                        + "\"genres\":[],"
                        + "\"copies\":3,"
                        + "\"available\":true"
                        + "}";

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this.videoUnderTest);

        assertEquals(json,actualJson,"JSON string produced should match given");
    }

    // deals with weird characters
    @Test
    public void serializesWithUnusualCharacters() throws Exception
    {
        videoUnderTest = new Video();
        videoUnderTest.id = 1L;
        videoUnderTest.title = "Æsthetic Wørld: Jøurney tø 4ëvër";
        videoUnderTest.image = "rohan.jpg";
        videoUnderTest.genres = Set.of("Action");
        videoUnderTest.copies = 3;

        String actualJson =
                "{"
                        + "\"id\":1,"
                        + "\"title\":\"Æsthetic Wørld: Jøurney tø 4ëvër\","
                        + "\"image\":\"rohan.jpg\","
                        + "\"genres\":[\"Action\"],"
                        + "\"copies\":3,"
                        + "\"available\":true"
                        + "}";

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this.videoUnderTest);

        assertEquals(json,actualJson,"JSON string produced should match given");
    }

    @Test
    public void serializesFilePaths() throws Exception
    {
        videoUnderTest = new Video();
        videoUnderTest.id = 1L;
        videoUnderTest.title = "The Lord of the Rings: The Two Towers";
        videoUnderTest.image = "filepath/middle/earth/rohan.jpg";
        videoUnderTest.genres = Set.of("Action");
        videoUnderTest.copies = 3;

        String actualJson =
                "{"
                        + "\"id\":1,"
                        + "\"title\":\"The Lord of the Rings: The Two Towers\","
                        + "\"image\":\"filepath/middle/earth/rohan.jpg\","
                        + "\"genres\":[\"Action\"],"
                        + "\"copies\":3,"
                        + "\"available\":true"
                        + "}";

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this.videoUnderTest);

        assertEquals(json,actualJson,"JSON string produced should match given");
    }

    @Test
    public void serializesUnavailable() throws Exception
    {
        videoUnderTest = new Video();
        videoUnderTest.id = 1L;
        videoUnderTest.title = "The Lord of the Rings: The Two Towers";
        videoUnderTest.image = "rohan.jpg";
        videoUnderTest.genres = Set.of("Action");
        videoUnderTest.copies = 1;
        videoUnderTest.rentals = new HashSet<>();
        videoUnderTest.rentals.add(new Rental());

        String actualJson =
                "{"
                        + "\"id\":1,"
                        + "\"title\":\"The Lord of the Rings: The Two Towers\","
                        + "\"image\":\"rohan.jpg\","
                        + "\"genres\":[\"Action\"],"
                        + "\"copies\":1,"
                        + "\"available\":false"
                        + "}";

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this.videoUnderTest);

        assertEquals(json,actualJson,"JSON string produced should match given");
    }

    @Test
    public void serializesWithTwoGenres() throws Exception
    {
        videoUnderTest = new Video();
        videoUnderTest.id = 1L;
        videoUnderTest.title = "The Lord of the Rings: The Two Towers";
        videoUnderTest.image = "rohan.jpg";
        videoUnderTest.genres = Set.of("Action", "Fantasy");
        videoUnderTest.copies = 3;

        String set1 = "[\"Fantasy\",\"Action\"],";
        String set2 = "[\"Action\",\"Fantasy\"],";

        String actualJson1 =
                "{"
                        + "\"id\":1,"
                        + "\"title\":\"The Lord of the Rings: The Two Towers\","
                        + "\"image\":\"rohan.jpg\","
                        + "\"genres\":"
                        + set1
                        + "\"copies\":3,"
                        + "\"available\":true"
                        + "}";

        String actualJson2 =
                "{"
                        + "\"id\":1,"
                        + "\"title\":\"The Lord of the Rings: The Two Towers\","
                        + "\"image\":\"rohan.jpg\","
                        + "\"genres\":"
                        + set2
                        + "\"copies\":3,"
                        + "\"available\":true"
                        + "}";

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(this.videoUnderTest);

        assertTrue(json.equals(actualJson1) || json.equals(actualJson2),"JSON string produced should match given");
    }

    @Test
    public void deserializesAllFields() throws Exception
    {
        String actualJson =
                "{"
                        + "\"id\":1,"
                        + "\"title\":\"The Lord of the Rings: The Two Towers\","
                        + "\"image\":\"rohan.jpg\","
                        + "\"genres\":[\"Action\",\"Fantasy\"],"
                        + "\"copies\":3"
                        + "}";

        ObjectMapper mapper = new ObjectMapper();

        Video vid = mapper.readValue(actualJson,Video.class);

        assertEquals(vid.id,1L,"ID should be 1");
        assertEquals(vid.title,"The Lord of the Rings: The Two Towers","Title should be The Lord of the Rings: The Two Towers");
        assertEquals(vid.image,"rohan.jpg","image should be rohan.jpg");
        assertEquals(vid.genres, Set.of("Action", "Fantasy"),"genres should be Set with Action");
        assertEquals(vid.copies, 3,"copies should be 3");
    }

    //

    @Test
    public void deserializesWithMissingFields() throws Exception {
        String actualJson =
                "{"
                        + "\"id\":1,"
                        + "\"title\":\"The Lord of the Rings: The Two Towers\""
                        + "}";

        ObjectMapper mapper = new ObjectMapper();

        Video vid = mapper.readValue(actualJson, Video.class);

        assertEquals(vid.id, 1L, "ID should be 1");
        assertEquals(vid.title, "The Lord of the Rings: The Two Towers", "Title should be The Lord of the Rings: The Two Towers");
        assertNull(vid.image, "Image should be null");
        assertNull(vid.genres, "Genres should be null");
        assertEquals(vid.copies, null, "Copies should be null");
    }

    @Test
    void deserializeWithNullValues() throws Exception {
        String json = "{"
                + "\"id\":null,"
                + "\"title\":null,"
                + "\"image\":null,"
                + "\"genres\":null,"
                + "\"copies\":null"
                + "}";

        ObjectMapper mapper = new ObjectMapper();
        Video video = mapper.readValue(json, Video.class);

        assertNull(video.id);
        assertNull(video.title);
        assertNull(video.image);
        assertNull(video.genres);
        assertNull(video.copies);
    }

    @Test
    void deserializeWithEmptyValues() throws Exception {
        String json = "{"
                + "\"id\":1,"
                + "\"title\":\"\","
                + "\"image\":\"\","
                + "\"genres\":[],"
                + "\"copies\":0"
                + "}";

        ObjectMapper mapper = new ObjectMapper();
        Video video = mapper.readValue(json, Video.class);

        assertEquals(1L, video.id);
        assertEquals("", video.title);
        assertEquals("", video.image);
        assertEquals(Set.of(), video.genres);
        assertEquals(0, video.copies);
    }

    @Test
    void deserializingUnrecognizedFieldsThrows() throws Exception {
        String json = "{"
                + "\"id\":1,"
                + "\"title\":\"The Lord of the Rings: The Two Towers\","
                + "\"image\":\"rohan.jpg\","
                + "\"genres\":[\"Action\"],"
                + "\"copies\":3,"
                + "\"extraField\":\"extra\""
                + "}";

        ObjectMapper mapper = new ObjectMapper();

        assertThrows(Exception.class, () -> {
            mapper.readValue(json, Video.class);
        });
    }


    @Test
    void deserializingInvalidDataTypesThrows() throws Exception {
        String json = "{"
                + "\"id\":\"1\","
                + "\"title\":12345,"
                + "\"image\":true,"
                + "\"genres\":\"Action\","
                + "\"copies\":\"3\""
                + "}";

        ObjectMapper mapper = new ObjectMapper();

        assertThrows(Exception.class, () -> {
            mapper.readValue(json, Video.class);
        });
    }

    // Integration with backend
    @Test
    void savesProperly() {

        videoUnderTest = new Video();

        videoUnderTest.title = "The Lord of the Rings: The Fellowship of the Ring";
        videoUnderTest.image = "https://lotr.com/fotr.jpg";
        Set<String> genres1 = Set.of("Action", "Fantasy", "Drama");
        videoUnderTest.genres = genres1;
        videoUnderTest.copies = 10;

        Video videoSentToBackend = videoRepository.save(videoUnderTest);

        Video videoRecievedFromBackend = videoRepository.findById(videoSentToBackend.id).orElse(null);

        assertEquals(videoRecievedFromBackend, videoUnderTest);
    }


    // Testing equals and hash code overrides, mostly for coverage reasons.

    @Test
    void testEqualsSameProperties() {
        Video video1 = new Video();
        video1.title = "The Lord of the Rings: The Fellowship of the Ring";
        video1.image = "https://lotr.com/fotr.jpg";
        video1.genres = Set.of("Action", "Fantasy", "Drama");
        video1.copies = 10;

        Video video2 = new Video();
        video2.title = "The Lord of the Rings: The Fellowship of the Ring";
        video2.image = "https://lotr.com/fotr.jpg";
        video2.genres = Set.of("Action", "Fantasy", "Drama");
        video2.copies = 10;

        // They should be equal because their properties are the same.
        assertEquals(video1, video2);
    }

    @Test
    void testEqualsDifferentProperties() {
        Video video1 = new Video();
        video1.title = "The Lord of the Rings: The Fellowship of the Ring";
        video1.image = "https://lotr.com/fotr.jpg";
        video1.genres = Set.of("Action", "Fantasy", "Drama");
        video1.copies = 10;

        Video video2 = new Video();
        video2.title = "The Matrix";
        video2.image = "https://matrix.com/matrix.jpg";
        video2.genres = Set.of("Action", "Sci-Fi");
        video2.copies = 10;

        // They should not be equal because their properties are different.
        assertNotEquals(video1, video2);
    }

    @Test
    void testHashCodeSameProperties() {
        Video video1 = new Video();
        video1.title = "The Lord of the Rings: The Fellowship of the Ring";
        video1.image = "https://lotr.com/fotr.jpg";
        video1.genres = Set.of("Action", "Fantasy", "Drama");
        video1.copies = 10;

        Video video2 = new Video();
        video2.title = "The Lord of the Rings: The Fellowship of the Ring";
        video2.image = "https://lotr.com/fotr.jpg";
        video2.genres = Set.of("Action", "Fantasy", "Drama");
        video2.copies = 10;

        // They should have the same hashCode because their properties are the same.
        assertEquals(video1.hashCode(), video2.hashCode());
    }

    @Test
    void testHashCodeDifferentProperties() {
        Video video1 = new Video();
        video1.title = "The Lord of the Rings: The Fellowship of the Ring";
        video1.image = "https://lotr.com/fotr.jpg";
        video1.genres = Set.of("Action", "Fantasy", "Drama");
        video1.copies = 10;

        Video video2 = new Video();
        video2.title = "The Matrix";
        video2.image = "https://matrix.com/matrix.jpg";
        video2.genres = Set.of("Action", "Sci-Fi");
        video2.copies = 10;

        // They should have different hashCodes because their properties are different.
        assertNotEquals(video1.hashCode(), video2.hashCode());
    }






}