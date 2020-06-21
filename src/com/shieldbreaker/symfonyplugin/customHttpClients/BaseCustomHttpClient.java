package com.shieldbreaker.symfonyplugin.customHttpClients;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

public abstract class BaseCustomHttpClient {

    protected CloseableHttpClient httpClient;

    protected abstract void createHttpClient();
    public abstract CloseableHttpResponse execute(HttpHost target, HttpRequestBase request) throws IOException;
    public void close() throws IOException {
        httpClient.close();
    }

}
