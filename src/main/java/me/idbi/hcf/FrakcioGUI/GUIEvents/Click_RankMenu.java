package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.AnvilGUI.AnvilItems;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.RM_Items;
import me.idbi.hcf.FrakcioGUI.Items.RPrio_Items;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankManagerInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankPriorityInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.playertools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_RankMenu implements Listener {
    private final static Main m = Main.getPlugin(Main.class);
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase("§8Rank Manager")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back())) {
            e.getWhoClicked().openInventory(MainInventory.mainInv((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if (e.getCurrentItem().isSimilar(RPrio_Items.priorityToggleButton())) {
            e.getWhoClicked().openInventory(RankPriorityInventory.officialInventory(((Player) e.getWhoClicked())));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }

        if (e.getCurrentItem().isSimilar(RM_Items.create())) {
            createRankGUI_Anvil((Player) e.getWhoClicked());
            GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
            return;
        }

        if(e.getCurrentItem().getType() != Material.PAPER) return;

        String rankName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());

        e.getWhoClicked().openInventory(RankManagerInventory.inv(rankName));
        GUI_Sound.playSound((Player) e.getWhoClicked(), "click");
    }

    public static void createRankGUI_Anvil(Player p) {
        new AnvilGUI.Builder()
                .onClose(player -> {                                               //called when the inventory is closing
                })
                .onComplete((player, text) -> {                                    //called when the inventory output slot is clicked
                    if(text.matches("^[0-9a-zA-Z]+$")) {
                        for(String blacklisted_word : Main.blacklistedRankNames){
                            if(text.toLowerCase().contains(blacklisted_word.toLowerCase())){
                                GUI_Sound.playSound(player,"error");
                                return AnvilGUI.Response.text(Messages.GUI_BAD_WORD.queue());
                            }
                        }
                        Main.Faction faction = playertools.getPlayerFaction(p);
                        assert faction != null;
                        Faction_Rank_Manager.CreateRank(faction, text);
                        p.sendMessage(Messages.GUI_RANK_CREATED.queue().replace("%rank%", text));
                        GUI_Sound.playSound(player,"success");
                        return AnvilGUI.Response.close();
                    } else {
                        GUI_Sound.playSound(player,"error");
                        return AnvilGUI.Response.text(Messages.GUI_INVALID_TYPE_TEXT.queue());
                    }
                })
                //.preventClose()//prevents the inventory from being closed
                .text(Messages.GUI_CREATE_RANK_TEXT.queue())                              //sets the text the GUI should start with
                .itemLeft(AnvilItems.left())                      //use a custom item for the first slot
                //.itemRight(AnvilItems.done())                     //use a custom item for the second slot
                .plugin(m)                                          //set the plugin instance
                .open(p);
    }
}
