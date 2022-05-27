package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
import org.bukkit.entity.Player;

public class Faction_Ranks {
    public static void addRank(Player p, String name){
        if(!playertools.getMetadata(p,"factionid").equals("0")){
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p,"factionid")));
            // ToDo: Check rank exists
            if(playertools.hasPermission(p, rankManager.Permissions.ALL)){
                rankManager.CreateNewRank(faction,name);
                p.sendMessage(Messages.FACTION_CREATE_RANK.queue());
            }else{
                p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        }else{
            p.sendMessage(Messages.NOT_IN_FACTION.queue());
        }
    }
    public static void setPermissionToRank(Player p, String name,String id){
        if(!playertools.getMetadata(p,"factionid").equals("0")){
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p,"factionid")));
            if(playertools.hasPermission(p, rankManager.Permissions.ALL)){
                rankManager.Faction_Rank rank = faction.FindRankByName(name);
                if(rank != null){
                    try{
                        rankManager.Permissions perm = rankManager.Permissions.valueOf(id.toUpperCase());
                        rank.addPermission(perm);
                    }catch (Exception e){
                        //Todo: Balfasz vagy, és elírtad a perm nevét
                    }

                }else{
                    //Todo: Rank nem található ezzel a névvel
                }
            }else{
                p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        }else{
            p.sendMessage(Messages.NOT_IN_FACTION.queue());
        }
    }
    public static void removePermissionFromRank(Player p, String name,String id){
        if(!playertools.getMetadata(p,"factionid").equals("0")){
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p,"factionid")));
            if(playertools.hasPermission(p, rankManager.Permissions.ALL)){
                rankManager.Faction_Rank rank = faction.FindRankByName(name);
                if(rank != null){
                    try{
                        rankManager.Permissions perm = rankManager.Permissions.valueOf(id.toUpperCase());
                        rank.removePermission(perm);
                    }catch (Exception e){
                        //Todo: Balfasz vagy, és elírtad a perm nevét
                    }

                }else{
                    //Todo: Rank nem található ezzel a névvel
                }
            }else{
                p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        }else{
            p.sendMessage(Messages.NOT_IN_FACTION.queue());
        }
    }
}
