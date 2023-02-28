package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.AnvilGUI.AnvilItems;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RM_Items;
import me.idbi.hcf.FrakcioGUI.KickConfirm.DeleteRankConfirm;
import me.idbi.hcf.FrakcioGUI.Menus.RankMenuInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankPermissionInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_RankManager implements Listener {
    private final static Main m = Main.getPlugin(Main.class);

    public static void anvilRename(Player p, String rankName) {
        new AnvilGUI.Builder()
                .onClose(player -> {                                               //called when the inventory is closing
                })
                .onComplete((player, text) -> {                                    //called when the inventory output slot is clicked
                    if (text.matches("^[0-9a-zA-Z]+$")) {
                        for (String blacklisted_word : Main.blacklistedRankNames) {
                            if (text.toLowerCase().contains(blacklisted_word.toLowerCase())) {
                                GUI_Sound.playSound(player, GUI_Sound.HCFSounds.ERROR);
                                return AnvilGUI.Response.text(Messages.gui_bad_word.language(p).queue());
                            }
                        }
                        Faction faction = Playertools.getPlayerFaction(p);
                        assert faction != null;
                        FactionRankManager.rename(faction, rankName, text);
                        GUI_Sound.playSound(player, GUI_Sound.HCFSounds.SUCCESS);
                        return AnvilGUI.Response.close();
                    } else {
                        GUI_Sound.playSound(player, GUI_Sound.HCFSounds.ERROR);
                        return AnvilGUI.Response.text(Messages.gui_invalid_type_text.language(p).queue());
                    }
                })
                //.preventClose()//prevents the inventory from being closed
                .text(Messages.gui_rename_text.language(p).queue())                              //sets the text the GUI should start with
                .itemLeft(AnvilItems.left())                      //use a custom item for the first slot
                //.itemRight(AnvilItems.done())                     //use a custom item for the second slot
                .plugin(m)                                          //set the plugin instance
                .open(p);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith(" Rank")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(RankMenuInventory.inv((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.BACK);
            return;
        }

        String rankName = ChatColor.stripColor(e.getView().getTitle().split(" ")[0]);

        Faction f = Playertools.getPlayerFaction((Player) e.getWhoClicked());


        if (e.getCurrentItem().isSimilar(RM_Items.rename(((Player) e.getWhoClicked())))) {
            anvilRename((Player) e.getWhoClicked(), rankName);
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.CLICK);
            return;
        }

        if (e.getCurrentItem().isSimilar(RM_Items.permissionManager(((Player) e.getWhoClicked())))) {
            assert f != null;
            e.getWhoClicked().openInventory(RankPermissionInventory.inv(((Player) e.getWhoClicked()), f.FindRankByName(rankName)));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.CLICK);
            return;
        }

        if (e.getCurrentItem().isSimilar(RM_Items.deleteRank(((Player) e.getWhoClicked())))) {
            assert f != null;
            FactionRankManager.Rank rank = f.FindRankByName(rankName);
            if (rank.isLeader) {
                return;
            } else if (rank.isDefault) {
                return;
            }

            e.getWhoClicked().openInventory(DeleteRankConfirm.inv(rankName));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.CLICK);
        }
    }
}
