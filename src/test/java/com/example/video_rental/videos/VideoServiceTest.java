package com.example.video_rental.videos;

import com.example.video_rental.users.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class VideoServiceTest {

    @Mock
    AccountService accountService;

    @Mock
    private VideoRepository repo;

    // injects mock repo when created
    @InjectMocks
    private VideoService serviceUnderTest;

    @BeforeEach
    void setUp() throws Exception
    {
        // initializes the @Mock variables
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getVideoShouldGetVideoIfExists() {

        // Arrange
        Video video = new Video();
        video.id = 1L;
        video.title = "The Lord of the Rings: The Fellowship of the Ring";
        video.image = "https://lotr.com/fotr.jpg";
        Set<String> genres1 = Set.of("Action", "Fantasy", "Drama");
        video.genres = genres1;
        video.copies = 10;

        when(repo.findById(1L)).thenReturn(Optional.of(video));

        // Act
        Optional<Video> result = repo.findById(1L);

        // Assert
        assertEquals(video, result.get());
        assertTrue(result.isPresent());
    }

    @Test
    void getVideoShouldThrowExceptionIfMissing() throws VideoException {

        // Set up our fake repo to act the way a repo would act
        // Arrange
        when(repo.findById(1L)).thenReturn(Optional.empty());

        // Test our service method calls that stub
        // Act and Assert
        assertThrows(VideoException.VideoNotFoundException.class, () -> {
            serviceUnderTest.getVideo(1L);
        });
    }

    @Test
    void getAvailableVideoShouldGetOnlyGetVideoIfAvailable() {

        Video video = new Video();
        video.id = 1L;
        video.title = "The Lord of the Rings: The Fellowship of the Ring";
        video.image = "https://lotr.com/fotr.jpg";
        Set<String> genres1 = Set.of("Action", "Fantasy", "Drama");
        video.genres = genres1;
        video.copies = 10;

        when(repo.findById(1L)).thenReturn(Optional.of(video));

        Video result = serviceUnderTest.getAvailableVideo(1L);

        assertEquals(video, result);
    }

    @Test
    void getAvailableVideoShouldThrowIfNotAvailable() {

        Long videoId = 1L;
        Video video = mock(Video.class);

        when(video.isAvailable()).thenReturn(false);
        when(repo.findById(videoId)).thenReturn(Optional.of(video));

        assertThrows(VideoException.VideoNotFoundException.class, () -> {
            serviceUnderTest.getAvailableVideo(videoId);
        });
    }

    @Test
    void createVideo() {

    }

    @Test
    void findVideos() {
    }

    @Test
    void allVideos() {
    }

    @Test
    void updateVideo() {
    }
}