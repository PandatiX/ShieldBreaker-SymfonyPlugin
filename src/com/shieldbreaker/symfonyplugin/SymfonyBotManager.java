package com.shieldbreaker.symfonyplugin;

import com.shieldbreaker.bot.BotManager;
import com.shieldbreaker.kernel.ShieldBreaker;

public class SymfonyBotManager extends BotManager {
    public SymfonyBotManager() {
        super();
    }

    @Override
    public void displayStart() {
        shieldBreaker.out("Starting SymfonyBotManager", ShieldBreaker.OUT_PRIORITY.MEDIUM);
    }
}
