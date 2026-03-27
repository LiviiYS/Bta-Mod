package turniplabs.gameruleconfig.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandSource;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.gameruleconfig.GameruleConfigMod;
import turniplabs.gameruleconfig.config.GameruleConfigManager;

public class CommandGameruleConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameruleConfigMod.MOD_ID);

    public static void register() {
        CommandManager.DISPATCHER.register(
            CommandManager.literal("gameruleconfig")
                .then(CommandManager.literal("reload").executes(context -> {
                    try {
                        GameruleConfigManager.reloadConfig();
                        LOGGER.info("Gamerule config reloaded!");
                    } catch (Exception e) {
                        LOGGER.error("Error reloading config: {}", e.getMessage());
                    }
                    return 1; // success
                }))
                .then(CommandManager.literal("list").executes(context -> {
                    try {
                        LOGGER.info("========== Current Gamerule Configuration ==========");
                        GameruleConfigManager.getConfig().forEach((rule, value) ->
                            LOGGER.info("  {}: {}", rule, value)
                        );
                        LOGGER.info("====================================================");
                    } catch (Exception e) {
                        LOGGER.error("Error listing config: {}", e.getMessage());
                    }
                    return 1;
                }))
        );
    }
}