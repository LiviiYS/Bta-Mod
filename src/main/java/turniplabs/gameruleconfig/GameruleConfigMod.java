package turniplabs.gameruleconfig;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.data.gamerule.GameRuleBoolean;
import net.minecraft.core.data.gamerule.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;
import turniplabs.gameruleconfig.config.GameruleConfigManager;

public class GameruleConfigMod implements ModInitializer, RecipeEntrypoint, GameStartEntrypoint, DedicatedServerModInitializer {
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
		LOGGER.info("Gamerule Config Mod initialized.");
		GameruleConfigManager.loadConfig();
	}

	@Override
	public void onInitializeServer() {
		LOGGER.info("Gamerule Config Mod server initialized.");
		GameruleConfigManager.applyGamerulesToWorld();
	}

	@Override
	public void onRecipesReady() {
	}

	@Override
	public void initNamespaces() {
	}

	@Override
	public void beforeGameStart() {
	}

	@Override
	public void afterGameStart() {
	}
}