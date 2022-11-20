package me.idbi.hcf.particles;

import org.bukkit.Effect;
import org.bukkit.Location;

import java.util.ArrayList;


public class Shapes {
    public Shapes(int detail){
        theatres.clear();
        phis.clear();
        for(int i = 0; i <= detail; i++){
            theatres.add((2*Math.PI*i)/detail);
            phis.add((Math.PI*i)/detail);
        }
    }
    public static ArrayList<Double> theatres = new ArrayList<>();
    public static ArrayList<Double> phis = new ArrayList<>();
    public static void DrawCircle(Location center, double radius, double smoothness, Effect effect) {
        double angle = 0;
        while (angle < 360){
            double radiant_angle = Math.toRadians(angle);
            double angleX = center.getX() + Math.cos(radiant_angle) * radius;
            double angleZ = center.getZ() + Math.sin(radiant_angle) * radius;
            angle = angle + smoothness;
            center.getWorld().playEffect(new Location(center.getWorld(),angleX,center.getY(),angleZ), effect,effect.getId());
        }
    }
    public static void DrawSphere(Location center, double radius, double smoothness, Effect effect) {
        for(double theta : theatres) {
            for (double phi : phis) {
                double x, y, z;
                x = center.getX() + radius * Math.sin(phi) * Math.cos(theta);
                y = center.getY() + radius * Math.sin(phi) * Math.sin(theta);
                z = center.getZ() + radius * Math.cos(phi);
                center.getWorld().playEffect(new Location(center.getWorld(), x, y, z), effect, effect.getId());
            }
        }
    }
    public static void DrawCube(Location center, double radius, Effect effect) {

    }
    public static void DrawLine(Location start, Location end,Effect effect) {
        double minX = Math.min(start.getX(),end.getX());
        double maxX = Math.max(start.getX(),end.getX());
        double dx = end.getX() - start.getX();
        double dz = end.getZ() - start.getZ();
        double D = 2*dz- dx;
        double z = start.getZ();

        for(double x = minX; x<maxX; x++){
            start.getWorld().playEffect(new Location(start.getWorld(), x, start.getY(), z), effect, effect.getId());
            if(D > 0){
                z++;
                D = D - 2*dx;
            }
            D = D + 2*dz;
        }
    }

}
