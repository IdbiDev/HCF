package me.idbi.hcf.SubClaims;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.FactionRankManager;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;

public class SubClaimTool {

    public static boolean hasAccess(Player p, String rankName, Faction faction) {
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if(hcfPlayer.getFaction() == null) return false;
        if(hcfPlayer.getRank() == null) return false;

        FactionRankManager.Rank signRank = hcfPlayer.getFaction().getRank(rankName);
        if(signRank.getPriority() <= hcfPlayer.getRank().getPriority() && faction == hcfPlayer.getFaction()) {
            System.out.println("Van jogod! " + signRank.getName() + " " + hcfPlayer.getRank().getName());
            return true;
        }

        return false;
    }

    public static boolean isSubClaim(Sign sign) {
        String line0 = sign.getLine(0);
        String line1 = sign.getLine(1);
        String line2 = sign.getLine(2);
        String line3 = sign.getLine(3);

        if(!line2.isBlank() || !line3.isBlank()) return false;

        if(!line0.isBlank()) {
            if (line0.replace("&", "§").equalsIgnoreCase(Config.SubClaimTitle.asStr())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDoubleChest(Block block) {
        if(block.getType() != Material.CHEST) return false;
        boolean dbl = false;
        BlockFace[] aside = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        for(BlockFace bf : aside) {
            if(block.getRelative(bf, 1).getType() == Material.CHEST) {
                dbl = true;
                break;
            }
        }
        return dbl;
    }

    public static Sign getSign(Block block) {
        if(block.getType() != Material.CHEST) return null;
        BlockFace[] aside = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        for(BlockFace bf : aside) {
            Block elvilegSign = block.getRelative(bf, 1);
            if(elvilegSign.getType() == Material.WALL_SIGN) {
                Sign sign = (Sign) elvilegSign.getState();
                if (SubClaimTool.isSubClaim(sign)) {
                    return sign;
                }
                break;
            }
        }

        return null;
    }

    public static Block getDoubleChest(Block block) {
        if(block.getType() != Material.CHEST) return null;
        BlockFace[] aside = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        for(BlockFace bf : aside) {
            Block block2 = block.getRelative(bf, 1);
            if(block2.getType() == Material.CHEST) {
                return block2;
            }
        }
        return null;
    }

    public static boolean hasSubClaimBlock(Block chest) {
        if(chest.getType() == Material.CHEST) {
            Sign sign = getSign(chest);
            if(sign == null) return false;
            return true;
        }
        return false;
    }

    public static boolean hasSubClaim(Block chest) {
        if (chest.getType() != Material.CHEST)
            return false;

        if(isDoubleChest(chest)) {
            Block doubleC = getDoubleChest(chest);
            if(doubleC == null) return false;
            if(doubleC.getType() == Material.AIR) return false;
            if(hasSubClaimBlock(doubleC) || hasSubClaimBlock(chest)) {
                return true;
            }
        } else {
            return hasSubClaimBlock(chest);
        }
        return false;
    }
}
