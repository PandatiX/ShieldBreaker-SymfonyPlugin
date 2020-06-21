package com.shieldbreaker.symfonyplugin;

import com.shieldbreaker.cli.BaseParametersManager;
import com.shieldbreaker.cli.ParametersManager;
import com.shieldbreaker.symfonyplugin.exceptions.DualityProxyException;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.Option;

import java.util.Arrays;

public class SymfonyBotParametersManager extends BaseParametersManager {
    private static final String defaultProtocol = "https";
    private static final String defaultUserAgent = "Hidden";
    private static final int defaultPort = 443;

    public SymfonyBotParametersManager() {
        super();
        supportedProtocols = new String[]{"http", "https"};
    }

    @Override
    protected void createCliOptions() {
        Option socksProxy = new Option("sp", "socksProxy", true, "Set SOCKS 5 proxy address.");
        socksProxy.setArgName("IP:Port");
        socksProxy.setRequired(false);
        addOption(socksProxy, ParametersManager.KEYS.NO_KEY);

        Option httpProxy = new Option("hp", "httpProxy", true, "Set HTTP proxy address.");
        httpProxy.setArgName("IP:Port");
        httpProxy.setRequired(false);
        addOption(httpProxy, ParametersManager.KEYS.NO_KEY);

        Option protocol = new Option("pt", "protocol", true, "Set HTTP protocol (default is https).");
        protocol.setArgName("http/https");
        protocol.setRequired(false);
        addOption(protocol, defaultProtocol, ParametersManager.KEYS.NO_KEY);

        Option userAgent = new Option("ua", "userAgent", true, "Set the User-Agent (requests header).");
        userAgent.setRequired(false);
        addOption(userAgent, defaultUserAgent, ParametersManager.KEYS.NO_KEY);

        Option port = new Option("pr", "port", true, "Set server port (default is 443).");
        port.setArgName("PORT");
        port.setRequired(false);
        addOption(port, String.valueOf(defaultPort), ParametersManager.KEYS.NO_KEY);

        Option trialRoute = new Option("tr", "trialRoute", true, "Set trial route (i.e. /login_check).");
        trialRoute.setArgName("ROUTE");
        addOption(trialRoute, ParametersManager.KEYS.GUI_KEY);

        Option failureRoute = new Option("fr", "failureRoute", true, "Set failure route (i.e. /login).");
        failureRoute.setArgName("ROUTE");
        addOption(failureRoute, ParametersManager.KEYS.GUI_KEY);
    }

    @Override
    protected void checkParameters() throws DualityProxyException {
        //Check for Socks and HTTP proxies
        if (!getValue("socksProxy").isEmpty() && !getValue("httpProxy").isEmpty())
            throw new DualityProxyException();
    }

    @Override
    public void setValue(@NotNull String key, String value) {
        if (key.equals("port"))
            setPort(value);
        else if (key.equals("protocol"))
            setProtocol(value);
        else
            super.setValue(key, value);
    }

    private void setPort(String port) {
        int value = (port.matches("\\d+") ? Integer.parseInt(port) : defaultPort);
        value = (value > 0 ? value : defaultPort);
        super.setValue("port", String.valueOf(value));
    }

    private void setProtocol(String protocol) {
        String p;
        if (Arrays.asList(supportedProtocols).contains(protocol)) {
            p = protocol;
        } else {
            p = defaultProtocol;
        }
        super.setValue("protocol", p);
    }
}
