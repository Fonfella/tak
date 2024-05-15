package com.fonfella.tak.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ManageCardCommand {

    @NotEmpty
    String deviceName;

    String platformVersion;
    String udid;
    String url;
    String circuito;
    String afterVisa;
    int timeWait;
}
