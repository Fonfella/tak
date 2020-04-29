package com.vinz.tak.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RobotCommand {

    @NotEmpty
    String stoCazzo;
}
