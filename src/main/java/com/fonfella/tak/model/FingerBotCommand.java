package com.fonfella.tak.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class FingerBotCommand {

    @NotEmpty
    String deviceName;

    String platformVersion;
    String udid;
    String url;
}
