package com.vinz.tak.model;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class Pin {

    String did;

    @NotEmpty
    @Pattern(regexp="^[\\d]{4,}$")
    String pin;
}
