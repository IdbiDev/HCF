package me.idbi.hcf.FactionGUI.GUIEvents;

import me.idbi.hcf.AnvilGUI.AnvilItems;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import me.idbi.hcf.FrakcioGUI.Menus.Alley_MainInventory;
import me.idbi.hcf.FrakcioGUI.Menus.InviteManagerInventory;
import me.idbi.hcf.FrakcioGUI.Menus.MemberListInventory;
import me.idbi.hcf.FrakcioGUI.Menus.RankMenuInventory;
import me.idbi.hcf.HistoryGUI.History.FactionHistoryInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_MainInventory implements Listener {

    private static final Main m = Main.getPlugin(Main.class);

    public static void renameFaction(Player p) {
        new AnvilGUI.Builder()
                .onClose(player -> {                                               //called when the inventory is closing
                })
                .onComplete((player, text) -> {                                    //called when the inventory output slot is clicked
                    if (Playertools.isValidName(text)) {
                        Faction faction = Playertools.getPlayerFaction(p);
                        assert faction != null;
                        Playertools.renameFaction(faction, text);
                        //p.sendMessage("Renamed!");
                        GUISound.playSound(p, GUISound.HCFSounds.SUCCESS);
                        HCFPlayer.getPlayer(p).sendMessage(Messages.rename_faction);
                        return AnvilGUI.Response.close();
                    } else {
                        GUISound.playSound(p, GUISound.HCFSounds.ERROR);
                        return AnvilGUI.Response.text(Messages.gui_invalid_type_text.language(p).queue());

                    }
                })
                //.preventClose()//prevents the inventory from being closed
                .text(Messages.gui_rename_faction_text.language(p).queue())                              //sets the text the GUI should start with
                .itemLeft(AnvilItems.left())                      //use a custom item for the first slot
                //.itemRight(AnvilItems.done())                     //use a custom item for the second slot
                .plugin(m)                                          //set the plugin instance
                .open(p);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equalsIgnoreCase("§8Faction Manager")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;

        if (!(e.getWhoClicked() instanceof Player p)) return;

        //Player p = (Player) e.getWhoClicked();

        if (e.getCurrentItem().isSimilar(GUI_Items.rankManager(((Player) e.getWhoClicked())))) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_RANKS)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                GUISound.playSound(p, GUISound.HCFSounds.ERROR);
                return;

            }
            GUISound.playSound(p, GUISound.HCFSounds.CLICK);
            p.openInventory(RankMenuInventory.inv(p));
        } else if (e.getCurrentItem().isSimilar(GUI_Items.playerManager(((Player) e.getWhoClicked())))) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_PLAYERS)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                GUISound.playSound(p, GUISound.HCFSounds.ERROR);
                return;
            }
            GUISound.playSound(p, GUISound.HCFSounds.CLICK);
            p.openInventory(MemberListInventory.members(p));
        } else if (e.getCurrentItem().isSimilar(IM_Items.inviteManager(((Player) e.getWhoClicked())))) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_INVITE)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                GUISound.playSound(p, GUISound.HCFSounds.ERROR);
                return;
            }
            GUISound.playSound(p, GUISound.HCFSounds.CLICK);
            p.openInventory(InviteManagerInventory.inv(((Player) e.getWhoClicked())));
        } else if (e.getCurrentItem().isSimilar(GUI_Items.histories(((Player) e.getWhoClicked())))) {
            // ToDo: Logs check permission
            //if(!playertools.hasPermission(p, Faction_Rank_Manager.Permissions.MANAGE_INVITE)) {
             /*   p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
                GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
                return;
            }*/
            GUISound.playSound(p, GUISound.HCFSounds.CLICK);
            p.openInventory(FactionHistoryInventory.inv(p, Playertools.getPlayerFaction(p),
                    1, 1, 1, 1, 1, 1));
        } else if (e.getCurrentItem().isSimilar(GUI_Items.renameFaction(((Player) e.getWhoClicked())))) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                GUISound.playSound(p, GUISound.HCFSounds.ERROR);
                return;
            }
            GUISound.playSound(p, GUISound.HCFSounds.CLICK);
            renameFaction(p);
        } else if (e.getCurrentItem().isSimilar(Ally_Items.ally())) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                GUISound.playSound(p, GUISound.HCFSounds.ERROR);
                return;
            }
            GUI_Sound.playSound(p, GUI_Sound.HCFSounds.CLICK);
            p.openInventory(Alley_MainInventory.inv(((Player) e.getWhoClicked())));
        }
    }
}
