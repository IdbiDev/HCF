package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Tasks;
import me.idbi.hcf.Tools.TowerTools;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;

public class FactionMapCommand extends SubCommand {

    @Override
    public String getName() {
        return "map";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/faction map";
    }

    @Override
    public String getPermission() {
        return "factions.commands." + getName();
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {

        List<Claiming.Point> points = getMap(p);
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(p);
        if (!hcfPlayer.isViewMap()) {
            Tasks.executeAsync(() -> {
                TowerTools.createPillar(p, points);
                hcfPlayer.setViewMap(true);
            });
        } else {
            hcfPlayer.setViewMap(false);
            hcfPlayer.removeFactionViewMap(p);
        }

        addCooldown(p);
    }

    public List<Claiming.Point> getMap(Player p) {
        Location pLoc = p.getLocation();
        List<Claiming.Point> locs = new ArrayList<>();
        for (Faction f : Main.factionCache.values()) {
            if (f.getClaims().isEmpty()) continue;
            for (Claiming.Faction_Claim claim : f.getClaims()) {
                if (pLoc.distance(new Location(claim.getWorld(), claim.getStartX(), pLoc.getBlockY(), claim.getStartZ())) > (16 * Bukkit.getServer().getViewDistance())) continue;

                Claiming.Point bottom_left = new Claiming.Point(claim.getStartX(), claim.getStartZ());
                Claiming.Point top_right = new Claiming.Point(claim.getEndX(), claim.getEndZ());

                Claiming.Point top_left = new Claiming.Point(top_right.getX(), bottom_left.getZ());
                Claiming.Point bottom_right = new Claiming.Point(bottom_left.getX(), top_right.getZ());

                locs.add(bottom_left);
                locs.add(top_right);
                locs.add(top_left);
                locs.add(bottom_right);
            }
        }
        return locs;
    }
}
