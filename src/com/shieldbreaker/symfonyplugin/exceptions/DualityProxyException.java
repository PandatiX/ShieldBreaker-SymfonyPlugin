package com.shieldbreaker.symfonyplugin.exceptions;

public class DualityProxyException extends Exception {
    @Override
    public String getMessage() {
        return "SOCKS and HTTP proxies cannot be set at same time. Please check https://bitrebels.com/technology/difference-between-socks-proxy-http-proxy/";
    }
}
