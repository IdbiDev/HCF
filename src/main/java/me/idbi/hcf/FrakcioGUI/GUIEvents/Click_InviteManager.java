package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.AnvilGUI.AnvilItems;
import me.idbi.hcf.ClickableMessages.Clickable_Join;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Items.IM_Items;
import me.idbi.hcf.FrakcioGUI.Menus.InviteManagerInventory;
import me.idbi.hcf.FrakcioGUI.Menus.MainInventory;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.FactionHistorys.HistoryEntrys;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Playertools;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Date;

public class Click_InviteManager implements Listener {

    private static final Main m = Main.getPlugin(Main.class);

    public static void anvilInvite(Player p) {
        new AnvilGUI.Builder()
                .onClose(player -> {                                               //called when the inventory is closing
                })
                .onComplete((player, text) -> {                                    //called when the inventory output slot is clicked
                    if (Playertools.isValidName(text)) {
                        Faction faction = Playertools.getPlayerFaction(p);
                        assert faction != null;
                        Player target = Bukkit.getPlayer(text);
                        if (target != null) {
                            if (Playertools.getPlayerFaction(target) == null) {
                                if (!faction.isPlayerInvited(target)) {
                                    faction.invitePlayer(target);
                                    // Broadcast both player the invite successful
                                    faction.inviteHistory.add(new HistoryEntrys.InviteEntry(
                                            p.getName(),
                                            target.getName(),
                                            new Date().getTime(),
                                            true
                                    ));
                                    p.sendMessage(Messages.invited_player.language(p).setPlayer(target).queue());
                                    //target.sendMessage(Messages.INVITED_BY.repExecutor(p).setFaction(faction.factioname).queue());
                                    Clickable_Join.sendMessage(target,
                                            "/f join " + faction.getName(),
                                            Messages.invited_by.language(target).setExecutor(p).setFaction(faction).queue(),
                                            Messages.hover_join.language(target).queue());

                                    for (Player member : faction.getOnlineMembers()) {
                                        member.sendMessage(Messages.faction_invite_broadcast.language(member).setExecutor(p).setPlayer(target).queue());
                                    }

                                    GUI_Sound.playSound(p, GUI_Sound.HCFSounds.SUCCESS);
                                    GUI_Sound.playSound(target, GUI_Sound.HCFSounds.SUCCESS);
                                } else {
                                    // This player is already invited
                                    p.sendMessage(Messages.already_invited.language(p).queue());
                                    GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
                                }
                            } else {
                                p.sendMessage(Messages.player_in_faction.language(p).queue());
                                GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
                            }
                            //faction.invitePlayer(target);
                            return AnvilGUI.Response.close();
                        } else {
                            p.sendMessage(Messages.not_found_player.language(p).queue());
                            GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
                            return AnvilGUI.Response.close();
                        }
                    } else {
                        GUI_Sound.playSound(p, GUI_Sound.HCFSounds.ERROR);
                        return AnvilGUI.Response.text(Messages.gui_invalid_type_text.language(p).queue());
                    }
                })
                //.preventClose()//prevents the inventory from being closed
                .text(Messages.gui_invite_player.language(p).queue())                              //sets the text the GUI should start with
                .itemLeft(AnvilItems.left())                      //use a custom item for the first slot
                //.itemRight(AnvilItems.done())                     //use a custom item for the second slot
                .plugin(m)                                          //set the plugin instance
                .open(p);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().endsWith("§8Invite Manager")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(MainInventory.mainInv((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.BACK);
            return;
        }

        if (e.getCurrentItem().isSimilar(IM_Items.invitePlayers(((Player) e.getWhoClicked())))) {
            anvilInvite((Player) e.getWhoClicked());
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.CLICK);
            return;
        }

        if (e.getCurrentItem().isSimilar(IM_Items.invitedPlayers(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(InviteManagerInventory.invitedPlayers((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.CLICK);
        }
    }
}
