package com.shieldbreaker.symfonyplugin.exceptions;

public class InvalidHTTPStatusCodeException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid HTTP Status Code - Check for host settings.";
    }
}
