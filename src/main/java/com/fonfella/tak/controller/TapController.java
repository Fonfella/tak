package com.fonfella.tak.controller;

import com.fonfella.tak.model.StartRecord;
import com.fonfella.tak.model.Tape;
import com.fonfella.tak.service.TapService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/tap/record")//body con wait tempo attesa fino al completamento azione
    public Tape tap(@Valid @RequestBody StartRecord startRecord) throws InterruptedException, ExecutionException, IOException {

        return tapService.record(startRecord);
    }

    @PostMapping("/tap/play") //body con data e file compresso prodotto dal record per riproduzione
    public Tape tap(@Valid @RequestBody Tape tape) throws InterruptedException, ExecutionException, IOException {

        return tapService.play(tape);
    }
}
