package com.vinz.tak.controller;

import com.vinz.tak.model.Pin;
import com.vinz.tak.service.PinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PinController extends AbstractController {

    @Autowired
    private PinService pinService;

    @PostMapping("/pin")
    public Pin tap(@Valid @RequestBody Pin pin) {

        return pin;
    }
}
