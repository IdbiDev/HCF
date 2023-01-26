package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.CustomFiles.Comments.Messages;
import me.idbi.hcf.tools.Faction_Rank_Manager;
import me.idbi.hcf.tools.playertools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Faction_Claim {

    public static boolean PrepareClaiming(Player p) {
        if (!playertools.getMetadata(p, "factionid").equals("0")) {
            if (!playertools.hasPermission(p, Faction_Rank_Manager.Permissions.MANAGE_ALL)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                return false;
            }
            if (p.getInventory().firstEmpty() != -1) {
                ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
                ItemMeta meta = wand.getItemMeta();

                meta.setDisplayName("§6Claiming Wand");

                List<String> str = new ArrayList<>();

                str.addAll(Messages.claim_info.queueList());

                meta.setLore(str);
                wand.setItemMeta(meta);
                p.getInventory().setItem(p.getInventory().firstEmpty(), wand);

                //ListMessages.CLAIM_INFO.queue().forEach(p::sendMessage);
                playertools.setMetadata(p, "spawnclaiming", false);
                return true;
            } else {
                p.sendMessage(Messages.not_enough_slot.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());

        }

        return false;
    }
}
