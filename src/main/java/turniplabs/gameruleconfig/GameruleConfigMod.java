package turniplabs.gameruleconfig;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = GameruleConfigMod.MODID, name = GameruleConfigMod.NAME, version = GameruleConfigMod.VERSION)
public class GameruleConfigMod {
    public static final String MODID = "gameruleconfig";
    public static final String NAME = "Gamerule Config Mod";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        // Pre-initialization logic here
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Initialize the mod
        GameruleConfigManager.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Post-initialization logic here
    }
}