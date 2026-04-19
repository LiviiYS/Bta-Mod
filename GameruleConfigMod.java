// GameruleConfigMod.java

package my.package;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class GameruleConfigMod implements ModInitializer {

    @Override
    public void onInitialize() {
        // Initialization code for Babric/Fabric
        setupGameRules();
    }

    private void setupGameRules() {
        // Logic to set up game rules without Forge
    }
}