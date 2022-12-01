package me.idbi.hcf.events;

import me.idbi.hcf.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import static me.idbi.hcf.Main.sendCmdMessage;

public class PluginLoad implements Listener {

    @EventHandler
    public void onServerLoad(PluginEnableEvent event){

        if(event.getPlugin().getName().equalsIgnoreCase("HCFAbilities")) {
            Main.abilities_loaded = true;
            sendCmdMessage("§aHCF+ Abilities found,loaded.");
        }
        if(event.getPlugin().getName().equalsIgnoreCase("HCFCustomEnchants")) {
            Main.customenchants_loaded = true;
            sendCmdMessage("§aHCF+ CustomEnchantments found,loaded.");
        }

    }
}
