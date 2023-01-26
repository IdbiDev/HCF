package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.AnvilGUI.AnvilItems;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RM_Items;
import me.idbi.hcf.FrakcioGUI.KickConfirm.DeleteRankConfirm;
import me.idbi.hcf.FrakcioGUI.Menus.RankMenuInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankPermissionInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.tools.Objects.Faction;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.playertools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_RankManager implements Listener {
    private final static Main m = Main.getPlugin(Main.class);
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith(" Rank")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back())) {
            e.getWhoClicked().openInventory(RankMenuInventory.inv((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        String rankName = ChatColor.stripColor(e.getView().getTitle().split(" ")[0]);

        Faction f = playertools.getPlayerFaction((Player) e.getWhoClicked());


        if (e.getCurrentItem().isSimilar(RM_Items.rename())) {
            anvilRename((Player) e.getWhoClicked(), rankName);
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }

        if (e.getCurrentItem().isSimilar(RM_Items.permissionManager())) {
            assert f != null;
            e.getWhoClicked().openInventory(RankPermissionInventory.inv(f.FindRankByName(rankName)));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }

        if (e.getCurrentItem().isSimilar(RM_Items.deleteRank())) {
            assert f != null;
            Faction_Rank_Manager.Rank rank = f.FindRankByName(rankName);
            if(rank.isLeader) {
                return;
            } else if(rank.isDefault) {
                return;
            }

            e.getWhoClicked().openInventory(DeleteRankConfirm.inv(rankName));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }
    }

    public static void anvilRename(Player p, String rankName) {
        new AnvilGUI.Builder()
                .onClose(player -> {                                               //called when the inventory is closing
                })
                .onComplete((player, text) -> {                                    //called when the inventory output slot is clicked
                    if(text.matches("^[0-9a-zA-Z]+$")) {
                        for(String blacklisted_word : Main.blacklistedRankNames){
                            if(text.toLowerCase().contains(blacklisted_word.toLowerCase())){
                                GUI_Sound.playSound(player,"error");
                                return AnvilGUI.Response.text(Messages.gui_bad_word.language(p).queue());
                            }
                        }
                        Faction faction = playertools.getPlayerFaction(p);
                        assert faction != null;
                        Faction_Rank_Manager.RenameRank(faction,rankName,text);
                        GUI_Sound.playSound(player,"success");
                        return AnvilGUI.Response.close();
                    } else {
                        GUI_Sound.playSound(player,"error");
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
}
