package com.shieldbreaker.symfonyplugin.customHttpClients;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HttpHttpClient extends BaseCustomHttpClient {

    String address;
    RequestConfig config;

    public HttpHttpClient(String address) {
        this.address = address;
        createHttpClient();
    }

    @Override
    protected void createHttpClient() {
        this.httpClient = HttpClients.createDefault();

        RequestConfig.Builder configBuilder = RequestConfig.custom();

        String[] httpProxyParams = this.address.split(":");
        if (httpProxyParams.length != 2)
            throw new IllegalArgumentException("Invalid proxy schema");

        String httpProxyHostname = httpProxyParams[0];
        int httpProxyPort = Integer.parseInt(httpProxyParams[1]);

        config = configBuilder.setProxy(new HttpHost(httpProxyHostname, httpProxyPort)).build();
    }

    @Override
    public CloseableHttpResponse execute(HttpHost target, HttpRequestBase request) throws IOException {
        request.setConfig(config);
        return this.httpClient.execute(target, request);
    }
}
