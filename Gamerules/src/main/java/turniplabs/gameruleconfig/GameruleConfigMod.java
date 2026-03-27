package turniplabs.gameruleconfig;

import net.minecraft.mixin.Mixin;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.gameruleconfig.command.CommandGameruleConfig;
import turniplabs.gameruleconfig.config.GameruleConfigManager;

public class GameruleConfigMod implements ModInitializer, DedicatedServerModInitializer {
    public static final String MOD_ID = "gameruleconfig";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Register custom gamerules
    public static final GameRuleBoolean ALLOW_SLEEPING = GameRules.register(new GameRuleBoolean("allowSleeping", true));
    public static final GameRuleBoolean ALLOW_SPRINTING = GameRules.register(new GameRuleBoolean("allowSprinting", true));
    public static final GameRuleBoolean DO_DAYLIGHT_CYCLE = GameRules.register(new GameRuleBoolean("doDaylightCycle", true));
    public static final GameRuleBoolean DO_FIRE_SPREAD = GameRules.register(new GameRuleBoolean("doFireSpread", true));
    public static final GameRuleBoolean DO_NIGHTMARES = GameRules.register(new GameRuleBoolean("doNightmares", true));
    public static final GameRuleBoolean DO_SEASONAL_GROWTH = GameRules.register(new GameRuleBoolean("doSeasonalGrowth", true));
    public static final GameRuleBoolean DO_WEATHER_CYCLE = GameRules.register(new GameRuleBoolean("doWeatherCycle", true));
    public static final GameRuleBoolean DWARF_MODE = GameRules.register(new GameRuleBoolean("dwarfMode", false));
    public static final GameRuleBoolean INSTANT_HEALING = GameRules.register(new GameRuleBoolean("instantHealing", false));
    public static final GameRuleBoolean KEEP_INVENTORY = GameRules.register(new GameRuleBoolean("keepInventory", false));
    public static final GameRuleBoolean MOB_GRIEFING = GameRules.register(new GameRuleBoolean("mobGriefing", true));
    public static final GameRuleBoolean TREECAPITATOR = GameRules.register(new GameRuleBoolean("treecapitator", false));

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing GameruleConfigMod...");
        GameruleConfigManager.loadConfig();

        // Sync gamerules with config after loading
        syncGamerulesWithConfig();

        // Register commands
        CommandGameruleConfig.register();

        LOGGER.info("GameruleConfigMod loaded successfully!");
    }

    @Override
    public void onInitializeServer() {
        // Apply gamerules to world at server start
        GameruleConfigManager.applyGamerulesToWorld();
    }

    // Sync gamerules with config values
    private void syncGamerulesWithConfig() {
        setGameruleFromConfig(ALLOW_SLEEPING, "allowSleeping");
        setGameruleFromConfig(ALLOW_SPRINTING, "allowSprinting");
        setGameruleFromConfig(DO_DAYLIGHT_CYCLE, "doDaylightCycle");
        setGameruleFromConfig(DO_FIRE_SPREAD, "doFireSpread");
        setGameruleFromConfig(DO_NIGHTMARES, "doNightmares");
        setGameruleFromConfig(DO_SEASONAL_GROWTH, "doSeasonalGrowth");
        setGameruleFromConfig(DO_WEATHER_CYCLE, "doWeatherCycle");
        setGameruleFromConfig(DWARF_MODE, "dwarfMode");
        setGameruleFromConfig(INSTANT_HEALING, "instantHealing");
        setGameruleFromConfig(KEEP_INVENTORY, "keepInventory");
        setGameruleFromConfig(MOB_GRIEFING, "mobGriefing");
        setGameruleFromConfig(TREECAPITATOR, "treecapitator");
    }

    private void setGameruleFromConfig(GameRuleBoolean gamerule, String configKey) {
        Object valueObj = GameruleConfigManager.getGamerule(configKey);
        if (valueObj instanceof Boolean) {
            boolean value = (Boolean) valueObj;
            gamerule.setValue(value);
            LOGGER.info("Set gamerule {} to {}", gamerule.getName(), value);
        } else {
            LOGGER.warn("Invalid or missing config value for {}. Using default: {}", configKey, gamerule.getValue());
        }
    }
}