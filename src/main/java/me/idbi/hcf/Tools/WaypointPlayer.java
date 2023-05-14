package me.idbi.hcf.Tools;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import lombok.Getter;
import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WaypointPlayer {

    @Getter private HCFPlayer hcfPlayer;
    @Getter private ArrayList<LCWaypoint> waypoints;
    @Getter private boolean enabledWaypoints;

    public WaypointPlayer(HCFPlayer hcfPlayer) {
        this.hcfPlayer = hcfPlayer;
        this.waypoints = new ArrayList<>();
        this.enabledWaypoints = false;
    }

    public void reload() {
        Player p = Bukkit.getPlayer(hcfPlayer.getUUID());
        if(p == null) return;

        for (LCWaypoint waypoint : this.waypoints) {
            LunarClientAPI.getInstance().removeWaypoint(p, waypoint);
        }
        this.waypoints.clear();
        if(this.enabledWaypoints) {
            setupDefaults();
            for (LCWaypoint wp : this.defaultWaypoints) {
                LunarClientAPI.getInstance().removeWaypoint(p, wp);
            }
            for (LCWaypoint waypoint : this.defaultWaypoints) {
                if(sameWorld(p, waypoint))
                    LunarClientAPI.getInstance().sendWaypoint(p, waypoint);
            }
        } else {
            for (LCWaypoint wp : this.defaultWaypoints) {
                LunarClientAPI.getInstance().removeWaypoint(p, wp);
            }
        }

        if(this.hcfPlayer.inFaction()) {
            Faction f = this.hcfPlayer.getFaction();
            updateHome(f.getHomeLocation());
            updateRally(f.getRallyPosition());
            updateFocus(f.getFocusedTeam());
        }
    }

    public void updateHome(Location loc) {
        Player player = Bukkit.getPlayer(hcfPlayer.getUUID());
        if(player == null) return;
        if (clearFactionWaypoints()) return;

        String[] str = Config.HomeWaypoint.asStr().split("; ");
        LCWaypoint wp = getWaypoint(str[0]);
        if(wp != null)
            LunarClientAPI.getInstance().removeWaypoint(player, wp);

        if(loc == null)
            return;

        LCWaypoint waypoint = new LCWaypoint(str[0],
                loc,
                java.awt.Color.decode(str[1]).getRGB(),
                true);

        if(sameWorld(player, waypoint))
            LunarClientAPI.getInstance().sendWaypoint(player, waypoint);

        this.waypoints.add(waypoint);
    }

    /**
     * You need to call this before you reset the focused team!
     * @param 'newest focused faction'
     */
    public void updateFocus(Faction newFaction) {
        Player player = Bukkit.getPlayer(hcfPlayer.getUUID());
        if(player == null) return;
        if (clearFactionWaypoints()) return;

        Faction ownFaction = hcfPlayer.getFaction();
        if(ownFaction.getFocusedTeam() == null && newFaction == null) return;
        if(newFaction == null) {
            LCWaypoint wp = getWaypoint(ownFaction.getFocusedTeam().getName());
            if(wp != null)
                LunarClientAPI.getInstance().removeWaypoint(player, wp);
            return;
        }
        Location loc = newFaction.getHomeLocation();
        if(loc == null) return;

        LCWaypoint waypoint = new LCWaypoint(newFaction.getName(),
                loc,
                java.awt.Color.decode(Config.FocusWaypointColor.asStr()).getRGB(),
                true);

        if(sameWorld(player, waypoint))
            LunarClientAPI.getInstance().sendWaypoint(player, waypoint);

        this.waypoints.add(waypoint);
    }

    public void disable() {
        Player p = Bukkit.getPlayer(hcfPlayer.getUUID());
        if(p == null) return;
        for (LCWaypoint waypoint : this.waypoints) {
            LunarClientAPI.getInstance().removeWaypoint(p, waypoint);
        }
        for (LCWaypoint waypoint : this.defaultWaypoints) {
            LunarClientAPI.getInstance().removeWaypoint(p, waypoint);
        }
    }

    public boolean clearFactionWaypoints() {
        if (!hcfPlayer.inFaction()) {
            Player player = Bukkit.getPlayer(hcfPlayer.getUUID());
            if(player == null) return false;
            for (LCWaypoint waypoint : this.waypoints) {
                LunarClientAPI.getInstance().removeWaypoint(player, waypoint);
            }
            this.waypoints.clear();
            return true;
        }
        return false;
    }

    public void createWaypoint(String name, Location loc, java.awt.Color color) {
        Player player = Bukkit.getPlayer(hcfPlayer.getUUID());
        if(player == null) return;

        if(loc == null) return;
        LCWaypoint waypoint = new LCWaypoint(name, loc, color.asRGB(), true);

        LunarClientAPI.getInstance().sendWaypoint(player, waypoint);
        waypoints.add(waypoint);
    }

    public void updateWaypoint(Faction faction) {
        Faction focusedTeam = hcfPlayer.getFaction().getFocusedTeam();
        boolean remove = false;
        if (faction == null) {
            if (focusedTeam == null) return;
            removeWaypoint(focusedTeam.getName());
            return;
        } else if (focusedTeam != null) {
            removeWaypoint(focusedTeam.getName());
        }

        if (faction == null) {
            faction = focusedTeam;
        }

        if (faction == null) return;

        if (focusedTeam != null && focusedTeam != faction) {
            remove = true;
        }

        Location home = faction.getHomeLocation();
        if(home == null) {
            remove = true;
        }
        if (remove && focusedTeam != null)
            removeWaypoint(focusedTeam.getName());

        createWaypoint(faction.getName(), home, Color.GREEN);
    }

    public void createWaypoint(Location loc) {
        Player player = Bukkit.getPlayer(this.hcfPlayer.getUUID());
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(this.hcfPlayer.getUUID());

        boolean remove = hcfPlayer.getFaction().getRallyPosition() != null;
        WaypointPlayer waypointPlayer = hcfPlayer.getWaypointPlayer();

        if (remove) waypointPlayer.removeWaypoint(Messages.faction_rally_waypoint_name.language(player).queue());
        createWaypoint(Messages.faction_rally_waypoint_name.language(player).queue(), loc, Color.PURPLE);
    }

    public boolean hasWaypoint(String name) {
        return waypoints.stream().anyMatch(wp -> wp.getName().equalsIgnoreCase(name));
    }

    public LCWaypoint getWaypoint(String name) {
        if(hasWaypoint(name)) {
            for (LCWaypoint waypoint : waypoints) {
                if(waypoint.getName().equalsIgnoreCase(name)) {
                    return waypoint;
                }
            }
        }
        return null;
    }

    public void removeWaypoint(String name) {
        Player player = Bukkit.getPlayer(hcfPlayer.getUUID());
        if(player == null) return;

        LCWaypoint lcWaypoint = getWaypoint(name);
        if(lcWaypoint == null) return;

        LunarClientAPI.getInstance().removeWaypoint(player, lcWaypoint);
        waypoints.remove(lcWaypoint);
    }

    public void showDefault() {
        this.enabledWaypoints = true;
        Player player = Bukkit.getPlayer(hcfPlayer.getUUID());
        if(player == null) return;

        for (LCWaypoint defaultWaypoint : getDefaultWaypoints()) {
            LunarClientAPI.getInstance().sendWaypoint(player, defaultWaypoint);
        }
    }

    public void hideDefaults() {
        Player player = Bukkit.getPlayer(hcfPlayer.getUUID());
        if(player == null) return;

        this.enabledWaypoints = false;
        for (LCWaypoint defaultWaypoint : getDefaultWaypoints()) {
            LunarClientAPI.getInstance().removeWaypoint(player, defaultWaypoint);
        }
    }

    /**
     *
     * @return new waypoints visible state
     */
    public boolean toggleWaypoints() {
        if(this.enabledWaypoints) {
            hideDefaults();
            this.enabledWaypoints = false;
        } else {
            showDefault();
            this.enabledWaypoints = true;
        }
        return this.enabledWaypoints;
    }

    private ArrayList<LCWaypoint> getDefaultWaypoints() {
        ArrayList<LCWaypoint> waypoints = new ArrayList<>();

        for (String wp : Config.LunarWaypoints.asStrList()) {
            // name; world; x, y, z; #ff00ff
            String[] args = wp.split("; ");
            String name = args[0];
            World world = Bukkit.getWorld(args[1]);
            Integer[] ints = Playertools.getInts(args[2].split(", "));
            Location loc = new Location(world, ints[0], ints[1], ints[2]);
            LCWaypoint waypoint = new LCWaypoint(name, loc, java.awt.Color.decode(args[3]).getRGB(), true);
            waypoints.add(waypoint);
        }
        return waypoints;
    }
}
