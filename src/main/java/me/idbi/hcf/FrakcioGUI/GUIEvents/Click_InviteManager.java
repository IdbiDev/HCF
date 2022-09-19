package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.AnvilGUI.AnvilItems;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import me.idbi.hcf.FrakcioGUI.Menus.InviteManagerInventory;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_InviteManager implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith("§8Invite Manager")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back())) {
            e.getWhoClicked().openInventory(MainInventory.mainInv((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), "back");
            return;
        }

        if(e.getCurrentItem().isSimilar(IM_Items.invitePlayers())) {
            anvilInvite((Player) e.getWhoClicked());
            return;
        }

        if(e.getCurrentItem().isSimilar(IM_Items.invitedPlayers())) {
            e.getWhoClicked().openInventory(InviteManagerInventory.invitedPlayers((Player) e.getWhoClicked()));
            return;
        }
    }

    private static Main m = Main.getPlugin(Main.class);

    public static void anvilInvite(Player p) {
        new AnvilGUI.Builder()
                .onClose(player -> {                                               //called when the inventory is closing
                })
                .onComplete((player, text) -> {                                    //called when the inventory output slot is clicked
                    if(text.matches("^[0-9a-zA-Z]+$")) {
                        Main.Faction faction = playertools.getPlayerFaction(p);
                        assert faction != null;
                        Player target = Bukkit.getPlayer(text);
                        if(target != null) {
                            if(playertools.getPlayerFaction(target) == null){
                                if (!faction.isPlayerInvited(target)) {
                                    faction.invitePlayer(target);
                                    // Broadcast both player the invite successful

                                    p.sendMessage(Messages.INVITED_PLAYER.repPlayer(target).queue());
                                    target.sendMessage(Messages.INVITED_BY.repExecutor(p).setFaction(faction.factioname).queue());
                                    faction.BroadcastFaction(Messages.FACTION_INVITE_BROADCAST.repExecutor(p).repPlayer(target).queue());
                                    GUI_Sound.playSound(p,"success");
                                    GUI_Sound.playSound(target,"success");
                                } else {
                                    // This player is already invited
                                    p.sendMessage(Messages.ALREADY_INVITED.queue());
                                    GUI_Sound.playSound(p,"error");
                                }
                            } else {
                                p.sendMessage(Messages.PLAYER_IN_FACTION.queue());
                                GUI_Sound.playSound(p,"error");
                            }
                            //faction.invitePlayer(target);
                            return AnvilGUI.Response.close();
                        } else {
                            p.sendMessage(Messages.NOT_FOUND_PLAYER.queue());
                            GUI_Sound.playSound(p,"error");
                            return AnvilGUI.Response.close();
                        }
                    } else {
                        GUI_Sound.playSound(p,"error");
                        return AnvilGUI.Response.text(Messages.GUI_INVALID_TYPE_TEXT.queue());
                    }
                })
                //.preventClose()//prevents the inventory from being closed
                .text(Messages.GUI_INVITE_PLAYER.queue())                              //sets the text the GUI should start with
                .itemLeft(AnvilItems.left())                      //use a custom item for the first slot
                //.itemRight(AnvilItems.done())                     //use a custom item for the second slot
                .plugin(m)                                          //set the plugin instance
                .open(p);
    }
}
