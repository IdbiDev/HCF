package me.idbi.hcf.commands.cmdFunctions;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.Messages;
import me.idbi.hcf.tools.playertools;
import me.idbi.hcf.tools.rankManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Faction_Ranks {
    public static void addRank(Player p, String name) {
        if (!playertools.getMetadata(p, "factionid").equals("0")) {
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
            if (playertools.hasPermission(p, rankManager.Permissions.ALL)) {
                rankManager.Faction_Rank rk;
                rk = rankManager.CreateNewRank(faction, name);
                if(rk == null) {
                    p.sendMessage(Messages.FACTION_RANK_EXISTS.queue());
                    return;
                }
                p.sendMessage(Messages.FACTION_CREATE_RANK.queue());
            } else {
                p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        } else {
            p.sendMessage(Messages.NOT_IN_FACTION.queue());
        }
    }

    public static void setPermissionToRank(Player p, String name, String id) {
        if (!playertools.getMetadata(p, "factionid").equals("0")) {
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
            if (playertools.hasPermission(p, rankManager.Permissions.ALL)) {
                rankManager.Faction_Rank rank = faction.FindRankByName(name);
                if (rank != null) {
                    try {
                        rankManager.Permissions perm = rankManager.Permissions.BASIC;
                        for (rankManager.Permissions cica : rankManager.Permissions.values()) {
                            if (cica.getValue().equals(id)) {
                                perm = cica;
                                break;
                            }
                        }
                        rank.addPermission(perm);

                    } catch (Exception e) {
                        //Todo: Balfasz vagy, és elírtad a perm nevét
                        e.printStackTrace();
                    }

                } else {
                    //Todo: Rank nem található ezzel a névvel
                    System.out.println("Rank nope");
                }
            } else {
                p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        } else {
            p.sendMessage(Messages.NOT_IN_FACTION.queue());
        }
    }

    public static void removePermissionFromRank(Player p, String name, String id) {
        if (!playertools.getMetadata(p, "factionid").equals("0")) {
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
            if (playertools.hasPermission(p, rankManager.Permissions.ALL)) {
                rankManager.Faction_Rank rank = faction.FindRankByName(name);
                if (rank != null) {
                    try {
                        rankManager.Permissions perm = rankManager.Permissions.BASIC;
                        for (rankManager.Permissions cica : rankManager.Permissions.values()) {
                            if (cica.getValue().equals(id)) {
                                perm = cica;
                                break;
                            }
                        }
                        rank.removePermission(perm);

                    } catch (Exception e) {
                        //Todo: Balfasz vagy, és elírtad a perm nevét
                    }
                } else {
                    //Todo: Rank nem található ezzel a névvel
                }
            } else {
                p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        } else {
            p.sendMessage(Messages.NOT_IN_FACTION.queue());
        }
    }

    public static void setPlayerForRank(Player p, String player, String rank) {
        if (!playertools.getMetadata(p, "factionid").equals("0")) {
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
            // ToDo: Check rank exists
            if (playertools.hasPermission(p, rankManager.Permissions.ALL)) {
                Player target = Bukkit.getPlayer(player);
                if (target != null) {
                    faction.ApplyPlayerRank(target, rank);
                } else {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(player);
                    faction.ApplyOfflinePlayerRank(offlinePlayer,rank);
                }

                //Todo: Sikeresen player set Rank geci
                p.sendMessage(Messages.FACTION_CREATE_RANK.queue());
            } else {
                p.sendMessage(Messages.NO_PERMISSION_IN_FACTION.queue());
            }
        } else {
            p.sendMessage(Messages.NOT_IN_FACTION.queue());
        }
    }
}
