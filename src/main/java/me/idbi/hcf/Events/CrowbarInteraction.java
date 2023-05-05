package me.idbi.hcf.Events;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Crowbar;
import me.idbi.hcf.Tools.Objects.HCFPermissions;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CrowbarInteraction implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if(e.getClickedBlock() == null) return;
            if(e.getItem() == null) return;
            if(!e.getItem().hasItemMeta()) return;
            ItemMeta im = e.getItem().getItemMeta();
            if(!im.hasDisplayName()) return;
            if(e.getItem().getType() != Material.getMaterial(Config.CrowbarMaterial.asStr())) return;
            if(e.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
            if(!Playertools.isInWarzone(e.getClickedBlock().getLocation())) {
                e.getPlayer().sendMessage(Messages.you_cant_do.language(e.getPlayer()).setFaction(Playertools.getWarzone()).queue());
                return;
            }
            if(im.getDisplayName().equalsIgnoreCase(Config.CrowbarName.asStr())) {
                if(!HCFPermissions.crowbar_use.check(e.getPlayer())) return;
                int maxPortalUses = Config.CrowbarPortalUses.asInt();
                int maxSpawnerUses = Config.CrowbarSpawnerUses.asInt();

/*
                System.out.println("Current spawner: " + Crowbar.getSpawnerUses(e.getItem()));
                System.out.println("Current portal: " + Crowbar.getPortalUses(e.getItem()));
*/

                if(e.getClickedBlock().getType() == Material.MOB_SPAWNER) {
                    Crowbar.addSpawnerUses(e.getItem());
                    if(Crowbar.getSpawnerUses(e.getItem()) >= maxSpawnerUses) {
                        breakItem(e.getPlayer());
                    }

                    e.getPlayer().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), new ItemStack(e.getClickedBlock().getType()));
                    e.getClickedBlock().setType(Material.AIR);

                }
                if(e.getClickedBlock().getType() == Material.ENDER_PORTAL_FRAME) {
                    Crowbar.addPortalUses(e.getItem());
                    if(Crowbar.getPortalUses(e.getItem()) >= maxPortalUses) {
                        breakItem(e.getPlayer());
                    }

                    e.getPlayer().getWorld().dropItemNaturally(e.getClickedBlock().getLocation(), new ItemStack(e.getClickedBlock().getType()));
                    e.getClickedBlock().setType(Material.AIR);
                }
            }
        }
    }

    private static void breakItem(Player p) {
        p.playSound(
                p.getLocation(),
                Sound.ITEM_BREAK, 1F, 1F);
        p.setItemInHand(null);
    }
}
