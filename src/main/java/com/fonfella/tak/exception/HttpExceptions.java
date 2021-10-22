package com.fonfella.tak.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class HttpExceptions
{
    public void Unauthorized(String message)
    {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
    }

    public void BadRequest(String message)
    {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
    }
}
