package com.example.video_rental.videos;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/videos")
public class VideoController
{
    private VideoService videoService;

    @Autowired
    public VideoController(VideoService videoService)
    {
        this.videoService = videoService;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Video> getAllVideos()
    {
        return this.videoService.allVideos();
    }

    @GetMapping("/genre/{genre}")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Video> getAllInGenre(@PathVariable("genre") String genre)
    {
        return this.videoService.findVideos(genre);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Video createVideo(@RequestParam String token, @RequestBody Video video)
    {
        return this.videoService.createVideo(token,video);
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    public Video GetVideo(@RequestParam("videoID") Long id)
    {
        return this.videoService.getVideo(id);
    }

}
