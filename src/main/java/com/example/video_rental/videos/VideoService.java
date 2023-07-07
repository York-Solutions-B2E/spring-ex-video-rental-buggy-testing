package com.example.video_rental.videos;


import com.example.video_rental.users.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService
{
    private VideoRepository repo;
    private AccountService accountService;
    @Autowired
    public VideoService(VideoRepository repo, AccountService accountService)
    {
        this.repo = repo;
        this.accountService = accountService;
    }

    public Video getVideo(Long id) throws VideoException
    {
        Optional<Video> video = this.repo.findById(id);
        if(video.isEmpty())
        {
            throw new VideoException.VideoNotFoundException("Could not find video with given ID");
        }
        return video.get();
    }
    public Video getAvailableVideo(Long id) throws VideoException
    {
        Video v = getVideo(id);
        if(!v.isAvailable())
        {
            throw new VideoException.VideoNotFoundException("Video is not available");
        }
        return v;
    }
    public Video createVideo(String token, Video v)
    {
        this.accountService.getAdminFromToken(token);
        if(v.copies < 0)
        {
            throw new VideoException.InvalidVideoException("Video must have at least 1 copy");
        }
        if(v.id != null)
        {
            Optional<Video> found = this.repo.findById(v.id);
            if(found.isPresent())
            {
                throw new VideoException.InvalidVideoException("Video ID already taken");
            }
        }
        this.repo.save(v);
        return v;
    }
    public Iterable<Video> findVideos(String genre)
    {
        return this.repo.findAllByGenre(genre);
    }
    public Iterable<Video> allVideos()
    {
        return this.repo.findAll();
    }
    public void updateVideo(Video v)
    {
        this.repo.save(v);
    }



}
