package turniplabs.gameruleconfig.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.minecraft.core.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.gameruleconfig.GameruleConfigMod;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GameruleConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameruleConfigMod.MOD_ID);
    private static final String CONFIG_DIR = "config/gameruleconfig";
    private static final String CONFIG_FILE = CONFIG_DIR + "/gamerules.json";
    private static Map<String, Object> gameruleConfig = new HashMap<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static void loadConfig() {
        try {
            File configDir = new File(CONFIG_DIR);
            if (!configDir.exists() && configDir.mkdirs()) {
                LOGGER.info("Created config directory: {}", CONFIG_DIR);
            }

            File configFile = new File(CONFIG_FILE);
            if (!configFile.exists()) {
                createDefaultConfig(configFile);
                LOGGER.info("Created default gamerule config file: {}", CONFIG_FILE);
                return;
            }

            try (FileReader reader = new FileReader(configFile)) {
                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                Map<String, Object> loadedConfig = GSON.fromJson(reader, type);
                gameruleConfig = loadedConfig != null ? loadedConfig : new HashMap<>();
                LOGGER.info("Loaded gamerule config with {} rules", gameruleConfig.size());
                logCurrentConfig();
            }
        } catch (IOException e) {
            LOGGER.error("Failed to load gamerule config: ", e);
            gameruleConfig = new HashMap<>();
        }
    }

    private static void createDefaultConfig(File file) throws IOException {
        Map<String, Object> defaults = new HashMap<>();
        // Default gamerules
        defaults.put("allowSleeping", true);
        defaults.put("allowSprinting", true);
        defaults.put("doDaylightCycle", true);
        defaults.put("doFireSpread", true);
        defaults.put("doNightmares", true);
        defaults.put("doSeasonalGrowth", true);
        defaults.put("doWeatherCycle", true);
        defaults.put("dwarfMode", false);
        defaults.put("instantHealing", false);
        defaults.put("keepInventory", false);
        defaults.put("mobGriefing", true);
        defaults.put("treecapitator", false);

        try (FileWriter writer = new FileWriter(file)) {
            GSON.toJson(defaults, writer);
            LOGGER.info("Default gamerule config created");
        }
    }

    public static Map<String, Object> getConfig() {
        return new HashMap<>(gameruleConfig);
    }

    public static Object getGamerule(String rule) {
        return gameruleConfig.getOrDefault(rule, null);
    }

    public static void setGamerule(String rule, Object value) {
        gameruleConfig.put(rule, value);
    }

    public static void saveConfig() {
        try {
            File configFile = new File(CONFIG_FILE);
            try (FileWriter writer = new FileWriter(configFile)) {
                GSON.toJson(gameruleConfig, writer);
                LOGGER.info("Saved gamerule config");
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save gamerule config: ", e);
        }
    }

    public static void reloadConfig() {
        loadConfig();
        GameruleConfigMod.applyGamerulesToWorld();
    }

    public static void applyGamerulesToWorld() {
        try {
            MinecraftServer server = MinecraftServer.getInstance();
            if (server == null) {
                LOGGER.warn("Server is null, cannot apply gamerules");
                return;
            }

            World world = null;
            try {
                world = (World) server.getClass().getMethod("getOverworld").invoke(server);
            } catch (Exception e1) {
                try {
                    java.lang.reflect.Field worldsField = server.getClass().getField("worlds");
                    Object[] worlds = (Object[]) worldsField.get(server);
                    if (worlds != null && worlds.length > 0) {
                        world = (World) worlds[0];
                    }
                } catch (Exception e2) {
                    LOGGER.warn("Could not access worlds: {}", e2.getMessage());
                }
            }

            if (world == null) {
                LOGGER.warn("World is null, cannot apply gamerules");
                return;
            }

            for (Map.Entry<String, Object> entry : gameruleConfig.entrySet()) {
                String ruleName = entry.getKey();
                Object ruleValue = entry.getValue();

                LOGGER.debug("Applying gamerule: {} with raw value: {}", ruleName, ruleValue);
                boolean value = Boolean.parseBoolean(ruleValue.toString());

                try {
                    switch (ruleName) {
                        case "allowSleeping":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.ALLOW_SLEEPING))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.ALLOW_SLEEPING, value);
                            break;
                        case "allowSprinting":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.ALLOW_SPRINTING))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.ALLOW_SPRINTING, value);
                            break;
                        case "doDaylightCycle":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.DO_DAYLIGHT_CYCLE))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_DAYLIGHT_CYCLE, value);
                            break;
                        case "doFireSpread":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.DO_FIRE_SPREAD))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_FIRE_SPREAD, value);
                            break;
                        case "doNightmares":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.DO_NIGHTMARES))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_NIGHTMARES, value);
                            break;
                        case "doSeasonalGrowth":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.DO_SEASONAL_GROWTH))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_SEASONAL_GROWTH, value);
                            break;
                        case "doWeatherCycle":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.DO_WEATHER_CYCLE))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_WEATHER_CYCLE, value);
                            break;
                        case "dwarfMode":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.DWARF_MODE))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.DWARF_MODE, value);
                            break;
                        case "instantHealing":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.INSTANT_HEALING))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.INSTANT_HEALING, value);
                            break;
                        case "keepInventory":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.KEEP_INVENTORY))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.KEEP_INVENTORY, value);
                            break;
                        case "mobGriefing":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.MOB_GRIEFING))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.MOB_GRIEFING, value);
                            break;
                        case "treecapitator":
                            if (world.getLevelData().getGameRules().hasRule(GameruleConfigMod.TREECAPITATOR))
                                world.getLevelData().getGameRules().setValue(GameruleConfigMod.TREECAPITATOR, value);
                            break;
                        default:
                            LOGGER.warn("Unknown gamerule: {}", ruleName);
                    }
                    LOGGER.info("Set gamerule {} to {}", ruleName, value);
                } catch (Exception e) {
                    LOGGER.debug("Could not apply gamerule {}: {}", ruleName, e.getMessage());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error applying gamerules: {}", e.getMessage(), e);
        }
    }
}