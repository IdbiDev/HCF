package me.idbi.hcf.Koth;

import me.idbi.hcf.Bossbar.BossbarTools;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Koth.GUI.KOTHItemManager;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Koth implements Listener {

    public static koth_area GLOBAL_AREA = null;
    public static Player GLOBAL_PLAYER = null;
    public static int GLOBAL_TIME;

    @EventHandler(priority = EventPriority.LOWEST)
    public static void KothMoveEvent(PlayerMoveEvent e) {
        //Ha van aktív koth
        if (!(e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockY() != e.getTo().getBlockY() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()))
            return;
        if (GLOBAL_AREA != null) {
            HCF_Claiming.Point start = new HCF_Claiming.Point(GLOBAL_AREA.claim.startX, GLOBAL_AREA.claim.startZ);
            HCF_Claiming.Point end = new HCF_Claiming.Point(GLOBAL_AREA.claim.endX, GLOBAL_AREA.claim.endZ);
            HCF_Claiming.Point point = new HCF_Claiming.Point(e.getTo().getBlockX(), e.getTo().getBlockZ());
            Player p = e.getPlayer();
            if (GLOBAL_PLAYER == null) {
                // ha kothba ment ÉS nem foglalja senki
                if (GLOBAL_AREA.claim.attribute.equals(HCF_Claiming.ClaimAttributes.KOTH) && HCF_Claiming.doOverlap(start, end, point, point)) {
                    HCFPlayer hcf = HCFPlayer.getPlayer(p);
                    if (hcf.inFaction()) {
                        GLOBAL_PLAYER = e.getPlayer();


                        Bukkit.broadcastMessage(Messages.koth_capturing_started.setFaction(GLOBAL_AREA.faction).queue());
                    }
                }
            } else {
                if (GLOBAL_AREA.claim.attribute.equals(HCF_Claiming.ClaimAttributes.KOTH) && !HCF_Claiming.doOverlap(start, end, point, point)) {
                    if (GLOBAL_PLAYER == e.getPlayer()) {
                        GLOBAL_PLAYER = null;
                        GLOBAL_TIME = Config.KOTHDuration.asInt() * 60;
                        Bukkit.broadcastMessage(Messages.koth_capturing_ended.language(p).setFaction(GLOBAL_AREA.faction).queue());
                    }
                }
            }

        }
    }

    public static Faction createKoth(String name) {
        return Playertools.createCustomFaction(name, "");
//        KOTH.koth_area temp = new KOTH.koth_area(
//                faction,
//                new HCF_Claiming.Point(claim.startX,claim.startZ),
//                new HCF_Claiming.Point(claim.endX,claim.endZ)
//        );
    }

    public static void startKoth(String f) {
        try {
            stopKoth();
            Main.sendCmdMessage("KOTH: " + Main.kothCache.get(f).faction.name);
            GLOBAL_AREA = Main.kothCache.get(f);
            String msg = "§4§n%s§f§4 KOTH STARTED!".replace("%s", Main.kothCache.get(f).faction.name);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.sendTitle(
                        msg,
                        "HCF+ UwU"
                );
                p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1f, 1f);
            }

        } catch (Exception e) {
            Main.sendCmdMessage("Nem található koth ezzel a névvel..");
        }
    }

    public static void StartRandomKoth() {
        if (Main.kothCache.isEmpty()) {
            Main.sendCmdMessage("§4 NO KOTH BITCH");
            return;
        }
        int c = new Random().nextInt(Main.kothCache.size());
        List<String> names = new ArrayList<>(Main.kothCache.keySet());
        startKoth(names.get((c == 0 && names.size() > 0) ? c : c - 1));
    }

    public static void stopKoth() {
        GLOBAL_AREA = null;
        //TODO:  Koth Elfoglalta xy
        //TODO: Set the items
        KOTHItemManager.addRewardsToPlayer(GLOBAL_PLAYER);
        GLOBAL_PLAYER = null;
        GLOBAL_TIME = Config.KOTHDuration.asInt() * 60;

        for (Player player : Bukkit.getOnlinePlayers()) {
            BossbarTools.remove(player);
        }
        ///Main.autoKoth.StopAutoKoth();
    }

    public static int getKothFromName(String name) {
        Faction k = Main.nameToFaction.get(name);
        if (k != null) {
            return k.id;
        }
        return 0;
    }

    public static class koth_area {
        public Faction faction;
        HCF_Claiming.Faction_Claim claim;

        public koth_area(Faction f, HCF_Claiming.Point start, HCF_Claiming.Point end, World world) {
            faction = f;
            claim = new HCF_Claiming.Faction_Claim(start.x, end.x, start.z, end.z, faction.id, HCF_Claiming.ClaimAttributes.KOTH, world.getName());
        }
    }

}
