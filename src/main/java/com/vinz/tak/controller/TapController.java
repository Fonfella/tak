package com.vinz.tak.controller;

import com.vinz.tak.model.StartRecord;
import com.vinz.tak.model.Tape;
import com.vinz.tak.service.TapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
public class TapController extends AbstractController {

    @Autowired
    private TapService tapService;

    @PostMapping("/tap/record")
    public Tape tap(@Valid @RequestBody StartRecord startRecord) throws InterruptedException, ExecutionException, IOException {

        return tapService.record(startRecord);
    }
}
