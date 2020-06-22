package com.shieldbreaker.symfonyplugin;

import com.shieldbreaker.bot.Bot;
import com.shieldbreaker.bot.BotManager;
import com.shieldbreaker.cli.BaseParametersManager;
import com.shieldbreaker.kernel.BasePlugin;

public class SymfonyPlugin implements BasePlugin {
    @Override
    public String getName() {
        return "SymfonyPlugin V1.1 - by PandatiX";
    }

    @Override
    public Class<? extends Bot> getClassBot() {
        return SymfonyBot.class;
    }

    @Override
    public Class<? extends BotManager> getClassBotManager() {
        return SymfonyBotManager.class;
    }

    @Override
    public Class<? extends BaseParametersManager> getClassParametersManager() {
        return SymfonyBotParametersManager.class;
    }
}
