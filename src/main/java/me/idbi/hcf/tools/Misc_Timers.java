package me.idbi.hcf.tools;

import me.idbi.hcf.Main;
import me.idbi.hcf.classes.Bard;
import me.idbi.hcf.classes.ClassSelector;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.util.Map;
import java.util.Objects;

public class Misc_Timers {
    private static final Connection con = Main.getConnection("timers.Misc");
    public static void CheckArmors(){
        new BukkitRunnable(){
            @Override
            public void run(){

                for( Player player : Bukkit.getServer().getOnlinePlayers()){
                    if(!player.hasMetadata("class")) continue;
                    ClassSelector.addClassToPlayer(player);
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class),0,60);
    }
    public static void addBardEffects(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(!player.hasMetadata("class")) continue;
                    if (playertools.getMetadata(player, "class").equals("bard"))
                        Bard.ApplyBardEffectOnActionBar(player);
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 90);
    }

    public static void DTR_Timer(){
        new BukkitRunnable(){
            @Override
            public void run(){
                for (Map.Entry<Integer, Long> entry : Main.DTR_REGEN.entrySet()) {
                    int key = entry.getKey();
                    long val = entry.getValue();
                    // 4000 <= 5000
                    if(val <= System.currentTimeMillis()){
                        Main.DTR_REGEN.remove(key); // lol
                        if(Main.debug)
                            System.out.println("DTR Regen >> Removed task cuz reached max DTR Faction: "+Main.factionToname.get(key));
                        SQL_Connection.dbExecute(con,"UPDATE factions SET DTR=5 WHERE ID='?'",String.valueOf(key));
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(Main.class),0,20);
    }

    public static void Bard_Energy(){
        new BukkitRunnable(){
            @Override
            public void run(){
                for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                    if(!player.hasMetadata("class")) continue;
                    if (Objects.equals(playertools.getMetadata(player, "class"), "bard"));
                    if(!(Double.parseDouble(Objects.requireNonNull(playertools.getMetadata(player, "bardenergy"))) >= 100f)){
                        playertools.setMetadata(player,"bardenergy",Double.parseDouble(playertools.getMetadata(player,"bardenergy")+0.1f));
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class),0,20);
    }

    public static void AutoSave(){
        new BukkitRunnable(){
            @Override
            public void run(){
                System.out.println("Autosave");
                Main.SaveAll();
            }
        }.runTaskTimer(Main.getPlugin(Main.class),6000,6000);
    }

}

