package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FactionClaimCommand extends SubCommand {
    @Override
    public String getName() {
        return "claim";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/f claim";
    }

    @Override
    public String getPermission() {
        return "factions.command." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (Playertools.getPlayerFaction(p) != null) {
            if (!Playertools.hasPermission(p, FactionRankManager.Permissions.MANAGE_ALL)) {
                p.sendMessage(Messages.no_permission.language(p).queue());
                return;
            }
            if (p.getInventory().firstEmpty() != -1) {
                ItemStack wand = new ItemStack(Material.DIAMOND_HOE);
                ItemMeta meta = wand.getItemMeta();

                meta.setDisplayName(Config.claiming_wand_name.asStr());
                meta.setLore(Config.claiming_wand_lore.asChatColorList());

                wand.setItemMeta(meta);
                p.getInventory().setItem(p.getInventory().firstEmpty(), wand);

                //ListMessages.CLAIM_INFO.queue().forEach(p::sendMessage);
                hcfPlayer.setClaimType(HCF_Claiming.ClaimTypes.FACTION);
            } else {
                p.sendMessage(Messages.not_enough_slot.language(p).queue());
            }
        } else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());

        }
    }
}
