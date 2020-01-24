package com.vinz.tak.controller;

import com.vinz.tak.service.TakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DebugController extends AbstractController
{

    @Autowired
    private TakService takService;

    @PostMapping("/debug")
    public <B> ResponseEntity<B> debug(@RequestBody Map<String, Object> values)
    {

        log.info("Debug: " + values.toString());

        takService.debug(values);

        return ResponseEntity.ok().build();
    }
}
