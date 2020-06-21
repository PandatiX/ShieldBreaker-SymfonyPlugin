package com.shieldbreaker.symfonyplugin.customHttpClients;

import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

public class Socks5HttpClient extends BaseCustomHttpClient {

    private String address;
    private HttpClientContext context;

    public Socks5HttpClient(String address) {
        this.address = address;
        createHttpClient();
    }

    @Override
    protected void createHttpClient() {
        //Setup connection manager based on sockets
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new MyConnectionSocketFactory(SSLContexts.createSystemDefault()))
                .build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(reg);

        //Setup SOCKS5 proxy
        context = HttpClientContext.create();

        //Setup SOCKS5 proxy
        String[] proxyParams = this.address.split(":");
        if (proxyParams.length != 2)
            throw new IllegalArgumentException("Invalid proxy address schema");

        String proxyHostname = proxyParams[0];
        int proxyPort = Integer.parseInt(proxyParams[1]);

        InetSocketAddress socksaddr = new InetSocketAddress(proxyHostname, proxyPort);
        context.setAttribute("socks.address", socksaddr);

        //Setup HTTP client
        httpClient = HttpClients.custom().setConnectionManager(cm).build();
    }

    @Override
    public CloseableHttpResponse execute(HttpHost target, HttpRequestBase request) throws IOException {
        return this.httpClient.execute(target, request, this.context);
    }

    static class MyConnectionSocketFactory extends SSLConnectionSocketFactory {

        public MyConnectionSocketFactory(final SSLContext sslContext) {
            super(sslContext);
        }

        @Override
        public Socket createSocket(final HttpContext context) {
            InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute("socks.address");
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
            return new Socket(proxy);
        }

    }
}
