package com.fonfella.tak.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class OtpYapFinderCommand {

    @NotEmpty
    String otpYapFinder;

}
