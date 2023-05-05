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

    public void createWaypoint(String name, Location loc, Color color) {
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
