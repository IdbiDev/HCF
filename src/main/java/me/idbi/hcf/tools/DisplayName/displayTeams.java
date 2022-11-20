package me.idbi.hcf.tools.DisplayName;

import com.mojang.authlib.GameProfile;
import me.idbi.hcf.Main;
import me.idbi.hcf.tools.playertools;
import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.TabPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class displayTeams implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        // setupDefaultName(e.getPlayer());
        /*setupDefaultName(e.getPlayer());
        changeName("§a" + e.getPlayer().getName(), e.getPlayer());*/
    }

    @EventHandler
    public void onBlockBreak(PlayerJoinEvent e) {
        // setupDefaultName(e.getPlayer());
    }

    public static void changeName(String name, Player player) {
        try {
            Method getHandle = player.getClass().getMethod("getHandle", (Class<?>[]) null);

            Object entityPlayer = getHandle.invoke(player);
            Class<?> entityHuman = entityPlayer.getClass().getSuperclass();
            Field bH = entityHuman.getDeclaredField("bH");

            bH.setAccessible(true);
            bH.set(entityPlayer, new GameProfile(player.getUniqueId(), name));

            for (Player players : Bukkit.getOnlinePlayers()) {
                if(playertools.isTeammate(players, players)) {
                    players.hidePlayer(player);
                    players.showPlayer(player);
                }
            }
        } catch (NoSuchMethodException | SecurityException | IllegalArgumentException | IllegalAccessException |
                 InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }


    public static void setupDefaultName(Player player) {
        TabPlayer tab = TabAPI.getInstance().getPlayer(player.getUniqueId());
        TabAPI.getInstance().getTeamManager().setPrefix(tab, "§c");

        Player adbi = Bukkit.getPlayer("adbi20014");
        TabPlayer tabadbi = TabAPI.getInstance().getPlayer(adbi.getUniqueId());

/*
        TabAPI.getInstance().getTeamManager().forceTeamName(tab, "asd1§a");
        TabAPI.getInstance().getTeamManager().forceTeamName(tabadbi, "asd1§a");
*/

        TabAPI.getInstance().getTeamManager().updateTeamData(tabadbi);
        TabAPI.getInstance().getTeamManager().updateTeamData(tab);
    }

    /*public static void setupAllTeams() {
        for (Map.Entry<Integer, Main.Faction> fac : Main.faction_cache.entrySet()) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                Scoreboard sb = p.getScoreboard();
                Team team = (sb.getTeam(fac.getValue().name) == null
                        ? sb.registerNewTeam(fac.getValue().name)
                        : sb.getTeam(fac.getValue().name));

                team.setPrefix("§a");
                team.setCanSeeFriendlyInvisibles(true);
                team.setAllowFriendlyFire(false);

               // p.getScoreboard().registerNewTeam(fac.getValue().factioname);
            }
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard sb = p.getScoreboard();
            if (sb.getTeam("non-faction") == null) {
                Team team = sb.registerNewTeam("non-faction");
                team.setPrefix("§c");
            }
            p.setScoreboard(sb);
        }
        refreshNons();
    }

    public static void setupAllTeams2() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard sb = p.getScoreboard();
            Team team = (sb.getTeam("non-faction") == null ? sb.registerNewTeam("non-faction") : sb.getTeam("non-faction"));
            team.setPrefix("§c");
            for (Player p2 : Bukkit.getOnlinePlayers()) {
                if (playertools.getMetadata(p2, "factionid").equals("0")) {
                    team.addEntry(p2.getName());
                }
            }
            p.setScoreboard(sb);
        }
    }

    public static void setupPlayer(Player p) {
        for(Map.Entry<Integer, Main.Faction> fac : Main.faction_cache.entrySet()) {
            Scoreboard sb = p.getScoreboard();
            Team team = (sb.getTeam(fac.getValue().name) == null
                    ? sb.registerNewTeam(fac.getValue().name)
                    : sb.getTeam(fac.getValue().name));

            team.setPrefix("§a");
            team.setCanSeeFriendlyInvisibles(true);
            team.setAllowFriendlyFire(false);

            //p.getScoreboard().registerNewTeam(fac.getValue().factioname);
        }

        Scoreboard sb = p.getScoreboard();
        if(sb.getTeam("non-faction") == null) {
            Team team = sb.registerNewTeam("non-faction");
            team.setPrefix("§c");
        }
        p.setScoreboard(sb);
    }

    public static void addToNonFaction(Player p) {
        for(Team team : p.getScoreboard().getTeams()) {
            team.removeEntry(p.getName());
        }

        Scoreboard sb = p.getScoreboard();
        Team team = (sb.getTeam("non-faction") == null ? sb.registerNewTeam("non-faction") : sb.getTeam("non-faction"));
        team.setPrefix("§c");
        p.setScoreboard(sb);
        refreshNons();
    }

    public static void refreshNons() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            if(!playertools.getMetadata(p, "factionid").equals("0")) continue;
            for(Team tim : p.getScoreboard().getTeams()) {
                tim.removeEntry(p.getName());
            }
            Scoreboard sb = p.getScoreboard();
            Team team = (sb.getTeam("non-faction") == null ? sb.registerNewTeam("non-faction") : sb.getTeam("non-faction"));
            team.setPrefix("§c");
            for(Player p2 : Bukkit.getOnlinePlayers()) {
                //if(playertools.getMetadata(p2, "factionid").equals("0")) {
                    for(Team tim : p2.getScoreboard().getTeams()) {
                        tim.removeEntry(p2.getName());
                    }
                    team.addEntry(p2.getName());
               // }
            }
            p.setScoreboard(sb);
        }
    }

    public static void addPlayerToTeam(Player p) {
        Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));

        Scoreboard sb = p.getScoreboard();

        Team team;

        for(Team tim : sb.getTeams()) {
            tim.removeEntry(p.getName());
        }

        if(sb.getTeam(faction.name) == null)
            team = getFaction(sb.registerNewTeam(faction.name));
        else
            team = sb.getTeam(faction.name);

        team.addEntry(p.getName());

        for(Player players : Bukkit.getOnlinePlayers()) {
            if(playertools.getMetadata(players, "factionid").equals(playertools.getMetadata(p, "factionid"))) {
                team.addEntry(players.getName());
            }
        }

        p.setScoreboard(sb);
    }

    public static void removeTeam(Main.Faction faction) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard sb = p.getScoreboard();
            sb.getTeam(faction.name).unregister();
        }
    }

    public static void refreshPlayer(Player p) {
        if(playertools.getMetadata(p, "factionid").equals("0")) {
            addToNonFaction(p);
            Bukkit.broadcastMessage("NON " + p.getName());
        } else {
            addPlayerToTeam(p);
            Bukkit.broadcastMessage("TEAM " + p.getName());
        }
    }

    public static void createTeam(Main.Faction faction) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            Scoreboard sb = p.getScoreboard();
            Team team = getFaction(sb.registerNewTeam(faction.name));
            team.setPrefix("§a");
            p.setScoreboard(sb);
        }
    }

    public static void removePlayerFromTeam(Player p) {
        try {
            Main.Faction faction = Main.faction_cache.get(Integer.parseInt(playertools.getMetadata(p, "factionid")));
            Scoreboard sb = Main.teams.get(faction);

            Team team;
            if(sb.getTeam(faction.name) == null)
                team = sb.registerNewTeam(faction.name);
            else
                team = sb.getTeam(faction.name);

            team.removeEntry(p.getName());
            addToNonFaction(p);
        } catch (NullPointerException ex) {
            //setupMembersAndTeams();
        }
    }

    public static Team getNonFaction(Team team) {
        team.setPrefix("§c");
        return team;
    }

    public static Team getFaction(Team team) {

        team.setPrefix("§a");
        team.setCanSeeFriendlyInvisibles(true);
        team.setAllowFriendlyFire(false);
        return team;
    }*/
}
