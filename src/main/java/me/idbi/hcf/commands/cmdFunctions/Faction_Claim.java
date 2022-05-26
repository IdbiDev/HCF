package me.idbi.hcf.commands.cmdFunctions;

import com.google.common.collect.ListMultimap;
import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.ListMessages;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Faction_Claim {

    public static boolean PrepareClaiming(Player p){
        if(!playertools.getMetadata(p,"factionid").equals("0")){
            if(!playertools.hasPermission(p, rankManager.Permissions.ALL)){
                //Todo: Message permisiion fos
                return false;
            }
            if(p.getInventory().firstEmpty() != -1){
                ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
                ItemMeta meta = wand.getItemMeta();

                List<String> str = Arrays.asList("Cicacsomag");

                meta.setLore(str);
                wand.setItemMeta(meta);
                p.getInventory().setItem(p.getInventory().firstEmpty(),wand);

                ListMessages.CLAIM_INFO.queue().forEach(p::sendMessage);
                return true;
            }else{
                p.sendMessage(Messages.NOT_ENOUGH_SLOT.queue());
            }
        }else{
            p.sendMessage(Messages.NOT_IN_FACTION.queue());

        }

        return false;
    }
}
