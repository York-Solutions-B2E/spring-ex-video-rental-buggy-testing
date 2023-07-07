package com.example.video_rental.videos;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

class VideoException extends RuntimeException
{
    public VideoException()
    {
    }

    public VideoException(String message)
    {
        super(message);
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Could not find video matching criteria")
    static class VideoNotFoundException extends VideoException
    {
        public VideoNotFoundException()
        {
        }

        public VideoNotFoundException(String message)
        {
            super(message);
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Video has invalid fields")
    static class InvalidVideoException extends VideoException
    {
        public InvalidVideoException()
        {
        }

        public InvalidVideoException(String message)
        {
            super(message);
        }
    }
}
