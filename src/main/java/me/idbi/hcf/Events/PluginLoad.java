package me.idbi.hcf.Events;

import me.idbi.hcf.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import static me.idbi.hcf.Main.sendCmdMessage;

public class PluginLoad implements Listener {

    @EventHandler
    public void onServerLoad(PluginEnableEvent event) {

        if (event.getPlugin().getName().equalsIgnoreCase("HCFAbilities")) {
            Main.abilitiesLoaded = true;
            sendCmdMessage("§aHCF+ Abilities found,loaded.");
        }
        if (event.getPlugin().getName().equalsIgnoreCase("HCFCustomEnchants")) {
            Main.customEnchantsLoaded = true;
            sendCmdMessage("§aHCF+ CustomEnchantments found,loaded.");
        }
        if (event.getPlugin().getName().equalsIgnoreCase("HCFDiscord")) {
            Main.discordLogLoaded = true;
            sendCmdMessage("§aHCF+ DiscordLogs found,loaded.");
        }


    }
}
