package com.vinz.tak.controller;

import com.vinz.tak.codec.IntListCodec;
import com.vinz.tak.model.Command;
import com.vinz.tak.service.TapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TapController extends AbstractController {

    @Autowired
    private TapService tapService;

    @Autowired
    private IntListCodec intListCodec;

    @PostMapping("/tap")
    public <B> ResponseEntity<B> tak(@RequestBody Command command) throws Exception {

        tapService.tap();

        return ResponseEntity.ok().build();
    }
}
