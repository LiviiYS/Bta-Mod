package turniplabs.gameruleconfig.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilderLiteral;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.core.net.command.CommandManager;
import net.minecraft.core.net.command.CommandSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.gameruleconfig.GameruleConfigMod;
import turniplabs.gameruleconfig.config.GameruleConfigManager;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CommandGameruleConfig implements CommandManager.CommandRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(GameruleConfigMod.MOD_ID);

	@Override
	public void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register((ArgumentBuilderLiteral) ArgumentBuilderLiteral
			.literal("gameruleconfig")
			.then(ArgumentBuilderLiteral
				.literal("reload")
				.executes(this::reloadConfig))
			.then(ArgumentBuilderLiteral
				.literal("list")
				.executes(this::listConfig))
		);
	}

	private int reloadConfig(CommandContext<Object> context) {
		try {
			GameruleConfigManager.reloadConfig();
			LOGGER.info("Gamerule config reloaded!");
		} catch (Exception e) {
			LOGGER.error("Error reloading config: " + e.getMessage());
		}
		return 1;
	}

	private int listConfig(CommandContext<Object> context) {
		try {
			LOGGER.info("========== Current Gamerule Configuration ==========");
			GameruleConfigManager.getConfig().forEach((rule, value) ->
				LOGGER.info("  " + rule + ": " + value)
			);
			LOGGER.info("====================================================");
		} catch (Exception e) {
			LOGGER.error("Error listing config: " + e.getMessage());
		}
		return 1;
	}
}