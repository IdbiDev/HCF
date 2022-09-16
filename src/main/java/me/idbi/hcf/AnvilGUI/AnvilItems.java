package me.idbi.hcf.AnvilGUI;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class AnvilItems {

    public static ItemStack left() {
        ItemStack is = new ItemStack(Material.PAPER);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§5");
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack done() {
        ItemStack is = new ItemStack(Material.NAME_TAG);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("§aDone");
        is.setItemMeta(im);
        return is;
    }
}
/*
new AnvilGUI.Builder()
    .onClose(player -> {                                               //called when the inventory is closing
        player.sendMessage("You closed the inventory.");
    })
    .onComplete((player, text) -> {                                    //called when the inventory output slot is clicked
        if(text.equalsIgnoreCase("you")) {
            player.sendMessage("You have magical powers!");
            return AnvilGUI.Response.close();
        } else {
            return AnvilGUI.Response.text("Incorrect.");
        }
    })
    .preventClose()                                                    //prevents the inventory from being closed
    .text("What is the meaning of life?")                              //sets the text the GUI should start with
    .itemLeft(new ItemStack(Material.IRON_SWORD))                      //use a custom item for the first slot
    .itemRight(new ItemStack(Material.IRON_SWORD))                     //use a custom item for the second slot
    .onLeftInputClick(player -> player.sendMessage("first sword"))     //called when the left input slot is clicked
    .onRightInputClick(player -> player.sendMessage("second sword"))   //called when the right input slot is clicked
    .title("Enter your answer.")                                       //set the title of the GUI (only works in 1.14+)
    .plugin(myPluginInstance)                                          //set the plugin instance
    .open(myPlayer);
 */