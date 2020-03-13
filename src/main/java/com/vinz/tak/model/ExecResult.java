package com.vinz.tak.model;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class ExecResult {

    int status;
    String message;
    @Singular List<Line> lines;

    @Data
    public static class Line  {

        long timestamp;
        String line;
    }
}
