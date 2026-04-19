package com.example.btamod;

public class GameruleConfigMod {
    
    public void onInitialize() {
        // Initialize the configuration
        GameruleConfigManager.init();  // Changed from loadConfig to init
    }
}