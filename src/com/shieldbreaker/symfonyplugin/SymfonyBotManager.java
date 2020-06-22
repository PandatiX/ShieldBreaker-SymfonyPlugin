package com.shieldbreaker.symfonyplugin;

import com.shieldbreaker.kernel.ShieldBreaker;
import com.shieldbreaker.webbruteforcecore.WebBruteForceBotManager;

public class SymfonyBotManager extends WebBruteForceBotManager {
    public SymfonyBotManager() {
        super();
    }

    @Override
    public void displayStart() {
        shieldBreaker.out("Starting SymfonyBotManager", ShieldBreaker.OUT_PRIORITY.MEDIUM);
    }
}
