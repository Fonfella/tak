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

    //esempio
//    http://localhost:8080/tap/record
//    {
//        "wait": 5000
//    }

    @PostMapping("/tap/record")//body con wait tempo attesa fino al completamento azione
    public Tape tap(@Valid @RequestBody StartRecord startRecord) throws InterruptedException, ExecutionException, IOException {

        return tapService.record(startRecord);
    }


    //esempio
//    http://10.130.146.18:9090/tap/play
//    {
//        "data": "eNpjYIADRiBmBmJLKN+GAVUOiBm9kPjocq445EBmmkK4TM+wyJlBuDybscgZQPmcWOQMccjhAuhueYnHPi489nGRad87PPZx47GPm0z7vpGnj5mJTH1FeOJ2G5lmfsRj5lbyzGTpwGPmWjxxxIMnjnjIcwurDx63zCXTzAt4zOwlz0y2WDxmltM3XbP9weOWRPrGH/syPG7xIc9MjlQ8ZprTN6w5TfG4RYG+Yc0lgMctvHjcwovHLbxkuuUFHrcw0DdcuM/idgv3T/LM5NmFx8xv9E2DvKvxuOUHfetwvhl43PKHvu0X/jQ8aZAVj1vYAW8QKRw="
//    }
    //data stringa che ritorna dal record corrisponde azioni registrate da replicare

    @PostMapping("/tap/play") //body con data e file compresso prodotto dal record per riproduzione
    public Tape tap(@Valid @RequestBody Tape tape) throws InterruptedException, ExecutionException, IOException {

        return tapService.play(tape);
    }
}
