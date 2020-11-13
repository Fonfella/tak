package com.vinz.tak.controller;

import com.vinz.tak.service.OtpYapService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OtpYapFinder extends AbstractController {

    @Autowired
    private OtpYapService otpYapService;


    @PostMapping("/otpyap")
    public JSONObject commandR(@Valid @RequestBody OtpYapFinder otpYapFinder)
    {
        log.info(otpYapFinder.toString());
        return otpYapService.sendOtpYapFinder(otpYapFinder);
    }
}
