package com.shieldbreaker.symfonyplugin;

import com.shieldbreaker.webbruteforcecore.WebBruteForceParametersManager;
import com.shieldbreaker.webbruteforcecore.exceptions.DualityProxyException;
import com.shieldbreaker.cli.ParametersManager;
import com.sun.istack.internal.NotNull;
import org.apache.commons.cli.Option;

public class SymfonyBotParametersManager extends WebBruteForceParametersManager {

    public SymfonyBotParametersManager() {
        super();
    }

    @Override
    protected void createCliOptions() {
        super.createCliOptions();

        Option trialRoute = new Option("tr", "trialRoute", true, "Set trial route (i.e. /login_check).");
        trialRoute.setArgName("ROUTE");
        addOption(trialRoute, ParametersManager.KEYS.GUI_KEY);

        Option failureRoute = new Option("fr", "failureRoute", true, "Set failure route (i.e. /login).");
        failureRoute.setArgName("ROUTE");
        addOption(failureRoute, ParametersManager.KEYS.GUI_KEY);
    }

    @Override
    protected void checkParameters() throws DualityProxyException {
        super.checkParameters();
    }

    @Override
    public void setValue(@NotNull String key, String value) {
        super.setValue(key, value);
    }
}
