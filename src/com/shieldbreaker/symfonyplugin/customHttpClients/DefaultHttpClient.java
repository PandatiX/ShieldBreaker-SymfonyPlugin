package com.shieldbreaker.symfonyplugin.customHttpClients;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class DefaultHttpClient extends BaseCustomHttpClient {

    public DefaultHttpClient() {
        createHttpClient();
    }

    @Override
    protected void createHttpClient() {
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public CloseableHttpResponse execute(HttpHost target, HttpRequestBase request) throws IOException {
        return this.httpClient.execute(target, request);
    }
}
