package com.pitonak.springcqrs.core.result;

import lombok.Data;

@Data
public class ErrorResult {

    private int code = 0;

    private String errorText = "";
}
