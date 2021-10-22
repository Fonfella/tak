package com.fonfella.tak.controller;

import com.fonfella.tak.model.Pin;
import com.fonfella.tak.service.PinService;
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

        return pinService.sendPin(pin);
    }
}
