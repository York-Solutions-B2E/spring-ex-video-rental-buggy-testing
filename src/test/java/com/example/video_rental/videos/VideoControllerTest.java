package com.example.video_rental.videos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Set;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VideoController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // for replacing beans in integration tests
    @MockBean
    private VideoService videoService;

    @Autowired
    private ObjectMapper objectMapper;

    // unit tests

//    @Test
//    void getAllVideosCallsServiceAllVideos() {
//        underTest.getAllVideos();
//        verify(underTest).getAllVideos().
//    }

    @Test
    void createVideoShouldCallServiceAndReturnExpectedVideo() {
        // Arrange
        // Making a mock instead of using the one wired with @MockBean to keep separated from spring context
        VideoService mockVideoService = mock(VideoService.class);
        VideoController underTest = new VideoController(mockVideoService);

        Video video = new Video();
        video.title = "The Lord of the Rings: The Fellowship of the Ring";
        video.image = "https://lotr.com/fotr.jpg";
        video.genres = Set.of("Action", "Fantasy", "Drama");
        video.copies = 10;
        String token = "mockToken";

        when(mockVideoService.createVideo(token, video)).thenReturn(video);

        // Act
        Video result = underTest.createVideo(token, video);

        // Assert
        assertEquals(video, result);
    }



    // integration test with client
    @Test
    void allEndpointShouldGetAllVideos() throws Exception {

        Video testVideo = new Video();
        testVideo.title = "The Lord of the Rings: The Fellowship of the Ring";
        testVideo.image = "https://lotr.com/fotr.jpg";
        Set<String> genres1 = Set.of("Action", "Fantasy", "Drama");
        testVideo.genres = genres1;
        testVideo.copies = 10;


        Video video2 = new Video();
        video2.title = "The Matrix";
        video2.image = "https://thematrix.com/matrix.jpg";
        Set<String> genres2 = Set.of("Action", "Sci-Fi");
        video2.genres = genres2;
        video2.copies = 5;

        Iterable<Video> testVideos = Arrays.asList(testVideo, video2);

        // BDD Mockito style
//        given(videoService.allVideos()).willReturn(testVideos);

        // Original Mockito style
        when(videoService.allVideos()).thenReturn(testVideos);

        ResultActions response = mockMvc.perform(get("/videos/all")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].title", is(testVideo.title)))
                .andExpect(jsonPath("$[0].image", is(testVideo.image)))
                .andExpect(jsonPath("$[0].copies", is(testVideo.copies)))
                .andExpect(jsonPath("$[0].genres", containsInAnyOrder("Action", "Fantasy", "Drama")))
                .andExpect(jsonPath("$[1].title", is(video2.title)))
                .andExpect(jsonPath("$[1].image", is(video2.image)))
                .andExpect(jsonPath("$[1].copies", is(video2.copies)))
                .andExpect(jsonPath("$[1].genres", containsInAnyOrder("Action", "Sci-Fi")));
    }


    @Test
    void getAllInGenre() {
    }

    // integration test with client

    @Test
    void createEndpointShouldReturnCreatedVideo() throws Exception {

        // given
        Video testVideo = new Video();
        testVideo.title = "The Lord of the Rings: The Fellowship of the Ring";
        testVideo.image = "https://lotr.com/fotr.jpg";
        Set<String> genres1 = Set.of("Action", "Fantasy", "Drama");
        testVideo.genres = genres1;
        testVideo.copies = 10;

        String mockToken = "mockToken";

        // Stubbing the createVideo method
        // Argument matchers allow you to specify conditions on the arguments of the method being stubbed.
        // willAnswer takes an "Answer" object, with which you can define more complex behaviour than returning  a fixed value.
        // In this case, it uses a lambda function as the answer object.
        // this function takes an InvocationOnMock object that mockito provides when the method is called
        // and returns the second argument of the invocation (the video object).
        // I could have done willReturn(testVideo) just as easily. This approach is more extensible and would work with any video object.
        given(videoService.createVideo(eq(mockToken), ArgumentMatchers.any(Video.class))).willAnswer(invocation -> invocation.getArgument(1));

        ResultActions response = mockMvc.perform(post("/videos/create?token=" + mockToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testVideo)));

        // used static import for MockMvcResultHandlers, hamcrest matchers and corematchers
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(testVideo.title)))
                .andExpect(jsonPath("$.image", is(testVideo.image)))
                .andExpect(jsonPath("$.copies", is(testVideo.copies)))
                .andExpect(jsonPath("$.genres", hasItems("Fantasy", "Action", "Drama")));
        // good way to show what is being submitted
//                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    void getVideo() {
    }
}