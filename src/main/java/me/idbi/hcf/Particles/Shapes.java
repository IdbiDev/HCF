package me.idbi.hcf.Particles;

import me.idbi.hcf.Tools.Claiming;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static me.idbi.hcf.Tools.Playertools.getDistanceBetweenPoints2D;


public class Shapes {
    public static ArrayList<Double> theatres = new ArrayList<>();
    public static ArrayList<Double> phis = new ArrayList<>();
    public Shapes(int detail) {
        theatres.clear();
        phis.clear();
        for (int i = 0; i <= detail; i++) {
            theatres.add((2 * Math.PI * i) / detail);
            phis.add((Math.PI * i) / detail);
        }
    }

    public static void DrawCircle(Location center, double radius, double smoothness, Effect effect) {
        double angle = 0;
        while (angle < 360) {
            double radiant_angle = Math.toRadians(angle);
            double angleX = center.getX() + Math.cos(radiant_angle) * radius;
            double angleZ = center.getZ() + Math.sin(radiant_angle) * radius;
            angle = angle + smoothness;
            center.getWorld().playEffect(new Location(center.getWorld(), angleX, center.getY(), angleZ), effect, effect.getId());
        }
    }

    public static void DrawCircle(Player player, Location center, double radius, double smoothness, Effect effect) {
        double angle = 0;
        while (angle < 360) {
            double radiant_angle = Math.toRadians(angle);
            double angleX = center.getX() + Math.cos(radiant_angle) * radius;
            double angleZ = center.getZ() + Math.sin(radiant_angle) * radius;
            angle = angle + smoothness;
            player.playEffect(new Location(center.getWorld(), angleX, center.getWorld().getHighestBlockYAt(center), angleZ), effect, effect.getId());
        }
    }

    public static void DrawCircle(Player player, double radius, double smoothness, Effect effect) {
        Location center = player.getLocation();
        double angle = 0;
        while (angle < 360) {
            double radiant_angle = Math.toRadians(angle);
            double angleX = center.getX() + Math.cos(radiant_angle) * radius;
            double angleZ = center.getZ() + Math.sin(radiant_angle) * radius;
            angle = angle + smoothness;
            player.playEffect(new Location(center.getWorld(), angleX, center.getWorld().getHighestBlockYAt(center), angleZ), effect, effect.getId());
        }
    }

    public static void DrawSphere(Location center, double radius, double smoothness, Effect effect) {
        for (double theta : theatres) {
            for (double phi : phis) {
                double x, y, z;
                x = center.getX() + radius * Math.sin(phi) * Math.cos(theta);
                y = center.getY() + radius * Math.sin(phi) * Math.sin(theta);
                z = center.getZ() + radius * Math.cos(phi);
                center.getWorld().playEffect(new Location(center.getWorld(), x, y, z), effect, effect.getId());
            }
        }
    }

    public static void Crossing(Location start, Location end, Effect effect, int res) {

        Claiming.Point bottom_left = new Claiming.Point(start.getBlockX(), start.getBlockZ());
        Claiming.Point top_right = new Claiming.Point(end.getBlockX(), end.getBlockZ());
        Claiming.Point top_left = new Claiming.Point(top_right.getX(), bottom_left.getZ());
        Claiming.Point bottom_right = new Claiming.Point(bottom_left.getX(), top_right.getZ());

        int width = getDistanceBetweenPoints2D(bottom_left, top_left) + 1;
        int height = getDistanceBetweenPoints2D(bottom_left, bottom_right) + 1;
        final double X_RES = (double) width / res;
        final double Z_RES = (double) height / res;
        //HCF_Claiming.Point l1, HCF_Claiming.Point r1, HCF_Claiming.Point l2, HCF_Claiming.Point r2
        int firstRectangle_Starting_X = bottom_left.getX();
        int firstRectangle_Starting_Z = bottom_left.getZ();

        int firstRectangle_Ending_X = top_right.getX();
        int firstRectangle_Ending_Z = top_right.getZ();

        int maxX = Math.max(firstRectangle_Starting_X, firstRectangle_Ending_X);
        int minX = Math.min(firstRectangle_Starting_X, firstRectangle_Ending_X);

        int maxZ = Math.max(firstRectangle_Starting_Z, firstRectangle_Ending_Z);
        int minZ = Math.min(firstRectangle_Starting_Z, firstRectangle_Ending_Z);

        for (int x = minX; x <= maxX; x += X_RES) {
            for (int z = minZ; z <= maxZ; z += Z_RES) {
                Location BigX = new Location(start.getWorld(), x, start.getWorld().getHighestBlockYAt(x, minZ), minZ);
                Location BigZ = new Location(start.getWorld(), minX, start.getWorld().getHighestBlockYAt(minX, z), z);
                for (Location loc : getLineBetweenPoints(BigX, BigZ)) {
                    loc.getWorld().playEffect(loc, effect, effect.getId());
                }
            }
        }

    }

    public static void DrawLine(Location start, Location end, Effect effect) {
        double minX = Math.min(start.getX(), end.getX());
        double maxX = Math.max(start.getX(), end.getX());
        double dx = end.getX() - start.getX();
        double dz = end.getZ() - start.getZ();
        double D = 2 * dz - dx;
        double z = start.getZ();

        for (double x = minX; x < maxX; x++) {

            if (D > 0) {
                z++;
                D = D - 2 * dx;
            }
            D = D + 2 * dz;
        }
    }

    public static ArrayList<Location> getLineBetweenPoints(Location start, Location end) {
        ArrayList<Location> returnLocation = new ArrayList<>();
        double deltaX = end.getX() - start.getX();
        double deltaZ = end.getZ() - start.getZ();
        int dxsign = (int) (Math.abs(deltaX) / deltaX);
        int dzsign = (int) (Math.abs(deltaZ) / deltaZ);
        double deltaError = Math.abs(deltaZ / deltaX);
        double error = 0;
        double z = start.getZ();
        for (int x = start.getBlockX(); x <= end.getBlockX(); x += dxsign) {
            error += deltaError;
            while (error >= 0.5) {
                z += dzsign;
                error--;
                returnLocation.add(new Location(start.getWorld(), x, start.getBlockY(), z));
            }
        }
        return returnLocation;
    }

}
