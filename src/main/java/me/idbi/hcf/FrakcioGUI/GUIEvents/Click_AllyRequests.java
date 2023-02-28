package me.idbi.hcf.FrakcioGUI.GUIEvents;

import me.idbi.hcf.AnvilGUI.AnvilItems;
import me.idbi.hcf.Commands.AllyCommands.AllyInviteCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.FrakcioGUI.GUI_Sound;
import me.idbi.hcf.FrakcioGUI.Items.Ally_Items;
import me.idbi.hcf.FrakcioGUI.Items.GUI_Items;
import me.idbi.hcf.FrakcioGUI.Menus.Alley_MainInventory;
import me.idbi.hcf.FrakcioGUI.Menus.Ally_AllyListInventory;
import me.idbi.hcf.Main;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class Click_AllyRequests implements Listener {

    private static final Main m = Main.getPlugin(Main.class);

    public static void requestAlly(Player p) {
        new AnvilGUI.Builder()
                .onClose(player -> {                                               //called when the inventory is closing
                })
                .onComplete((player, text) -> {                                    //called when the inventory output slot is clicked
                    if (text.matches("^[0-9a-zA-Z]+$")) {
                        GUI_Sound.playSound(player, GUI_Sound.HCFSounds.SUCCESS);
                        AllyInviteCommand.invite(p, text);
                        return AnvilGUI.Response.close();
                    } else {
                        GUI_Sound.playSound(player, GUI_Sound.HCFSounds.ERROR);
                        return AnvilGUI.Response.text(Messages.gui_invalid_type_text.language(p).queue());
                    }
                })
                //.preventClose()//prevents the inventory from being closed
                .text(Messages.gui_request_ally.language(p).queue())                              //sets the text the GUI should start with
                .itemLeft(AnvilItems.left())                      //use a custom item for the first slot
                //.itemRight(AnvilItems.done())                     //use a custom item for the second slot
                .plugin(m)                                          //set the plugin instance
                .open(p);
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().contains("§8Manage Requests")) return;

        e.setCancelled(true);

        if (e.getCurrentItem() == null) return;
        if (!e.getCurrentItem().hasItemMeta()) return;
        if (!e.getCurrentItem().getItemMeta().hasDisplayName()) return;

        if (e.getCurrentItem().isSimilar(GUI_Items.back(((Player) e.getWhoClicked())))) {
            e.getWhoClicked().openInventory(Alley_MainInventory.inv(((Player) e.getWhoClicked())));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.BACK);
            return;
        }

        if (e.getCurrentItem().isSimilar(Ally_Items.request())) {
            requestAlly((Player) e.getWhoClicked());
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.CLICK);
            return;
        }

        if (e.getCurrentItem().isSimilar(Ally_Items.sentRequests())) {

            e.getWhoClicked().openInventory(Ally_AllyListInventory.requestList((Player) e.getWhoClicked()));
            GUI_Sound.playSound((Player) e.getWhoClicked(), GUI_Sound.HCFSounds.CLICK);
        }
    }
}
