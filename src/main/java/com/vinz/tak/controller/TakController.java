package com.vinz.tak.controller;

import com.vinz.tak.model.Command;
import com.vinz.tak.service.TakService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TakController extends AbstractController
{

    @Autowired
    private TakService takService;

    @PostMapping("/tak")
    public <B> ResponseEntity<B> tak(@RequestBody Command command) throws Exception
    {
        String token = command.getToken();

        if (StringUtils.isEmpty(token) || !token.equalsIgnoreCase(takService.SECURITY_TOKEN)) {

            exceptions.Unauthorized("Invalid token [field: 'token']");
        }

        if (StringUtils.isEmpty(command.getServo())) {

            exceptions.BadRequest("You must select a servo id [field: 'servo']");
        }

        log.info("Tak command received: " + command.toString());

        takService.tak(command);

        return ResponseEntity.ok().build();
    }
}
