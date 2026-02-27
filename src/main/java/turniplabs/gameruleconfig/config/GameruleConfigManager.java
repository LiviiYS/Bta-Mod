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
			if (!configDir.exists()) {
				if (configDir.mkdirs()) {
					LOGGER.info("Created config directory: " + CONFIG_DIR);
				}
			}

			File configFile = new File(CONFIG_FILE);
			if (!configFile.exists()) {
				createDefaultConfig(configFile);
				LOGGER.info("Created default config file: " + CONFIG_FILE);
				return;
			}

			try (FileReader reader = new FileReader(configFile)) {
				Type type = new TypeToken<Map<String, Object>>() {}.getType();
				Map<String, Object> loadedConfig = GSON.fromJson(reader, type);
				gameruleConfig = loadedConfig != null ? loadedConfig : new HashMap<>();
				LOGGER.info("Loaded gamerule config with " + gameruleConfig.size() + " rules");
				logCurrentConfig();
			}
		} catch (IOException e) {
			LOGGER.error("Failed to load config: ", e);
			gameruleConfig = new HashMap<>();
		}
	}

	private static void createDefaultConfig(File file) throws IOException {
		Map<String, Object> defaults = new HashMap<>();

		// BTA Gamerules
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
			LOGGER.error("Failed to save config: ", e);
		}
	}

	public static void applyGamerulesToWorld() {
		try {
			MinecraftServer server = MinecraftServer.getInstance();
			if (server == null) {
				LOGGER.warn("Server is null, cannot apply gamerules");
				return;
			}

			// Use reflection to get worlds or try getOverworld method
			World world = null;
			try {
				// Try to get the overworld
				world = (World) server.getClass().getMethod("getOverworld").invoke(server);
			} catch (Exception e1) {
				try {
					// Fallback: try to access worlds field
					java.lang.reflect.Field worldsField = server.getClass().getField("worlds");
					Object[] worlds = (Object[]) worldsField.get(server);
					if (worlds != null && worlds.length > 0) {
						world = (World) worlds[0];
					}
				} catch (Exception e2) {
					LOGGER.warn("Could not access worlds: " + e2.getMessage());
				}
			}

			if (world == null) {
				LOGGER.warn("World is null, cannot apply gamerules");
				return;
			}

			for (Map.Entry<String, Object> entry : gameruleConfig.entrySet()) {
				String ruleName = entry.getKey();
				Object ruleValue = entry.getValue();

				try {
					boolean value = Boolean.parseBoolean(ruleValue.toString());

					// Apply each gamerule
					switch (ruleName) {
						case "allowSleeping":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.ALLOW_SLEEPING, value);
							LOGGER.info("Applied gamerule: allowSleeping = " + value);
							break;
						case "allowSprinting":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.ALLOW_SPRINTING, value);
							LOGGER.info("Applied gamerule: allowSprinting = " + value);
							break;
						case "doDaylightCycle":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_DAYLIGHT_CYCLE, value);
							LOGGER.info("Applied gamerule: doDaylightCycle = " + value);
							break;
						case "doFireSpread":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_FIRE_SPREAD, value);
							LOGGER.info("Applied gamerule: doFireSpread = " + value);
							break;
						case "doNightmares":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_NIGHTMARES, value);
							LOGGER.info("Applied gamerule: doNightmares = " + value);
							break;
						case "doSeasonalGrowth":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_SEASONAL_GROWTH, value);
							LOGGER.info("Applied gamerule: doSeasonalGrowth = " + value);
							break;
						case "doWeatherCycle":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.DO_WEATHER_CYCLE, value);
							LOGGER.info("Applied gamerule: doWeatherCycle = " + value);
							break;
						case "dwarfMode":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.DWARF_MODE, value);
							LOGGER.info("Applied gamerule: dwarfMode = " + value);
							break;
						case "instantHealing":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.INSTANT_HEALING, value);
							LOGGER.info("Applied gamerule: instantHealing = " + value);
							break;
						case "keepInventory":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.KEEP_INVENTORY, value);
							LOGGER.info("Applied gamerule: keepInventory = " + value);
							break;
						case "mobGriefing":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.MOB_GRIEFING, value);
							LOGGER.info("Applied gamerule: mobGriefing = " + value);
							break;
						case "treecapitator":
							world.getLevelData().getGameRules().setValue(GameruleConfigMod.TREECAPITATOR, value);
							LOGGER.info("Applied gamerule: treecapitator = " + value);
							break;
					}
				} catch (Exception e) {
					LOGGER.debug("Could not apply gamerule " + ruleName + ": " + e.getMessage());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error applying gamerules: " + e.getMessage(), e);
		}
	}

	public static void reloadConfig() {
		loadConfig();
		applyGamerulesToWorld();
	}

	private static void logCurrentConfig() {
		LOGGER.info("========== Current Gamerule Configuration ==========");
		for (Map.Entry<String, Object> entry : gameruleConfig.entrySet()) {
			LOGGER.info(entry.getKey() + ": " + entry.getValue());
		}
		LOGGER.info("====================================================");
	}
}