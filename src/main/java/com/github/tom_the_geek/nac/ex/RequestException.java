package com.github.tom_the_geek.nac.ex;

import java.io.IOException;

public class RequestException extends IOException {
    public RequestException(int code) {
        super("Request failed, received code " + code + " from server");
    }
}
