package me.idbi.hcf.Tools.Objects;

import me.idbi.hcf.CustomFiles.Messages.Messages;
import org.bukkit.entity.Player;

public enum HCFPermissions {

    elevator_use("factions.elevator.use"),
    elevator_create("factions.elevator.create"),
    slot_bypass("factions.admin.slotbypass"),
    eotw_join("factions.admin.eotwjoin"),
    //admin_customtimer("factions.commands.admin.customtimer"),
    signshop_use("factions.signshop.use"),
    signshop_create("factions.signshop.create"),
    crowbar_use("factions.crowbar.use"),
    deathban_bypass("factions.admin.deathbanbypass"),
    home_bypass("factions.admin.homebypass"),
    vanish_bypass("factions.admin.vanishbypass"),
    admin_spawn("factions.commands.admin.spawn");

    private String permission;

    HCFPermissions(String permission) {
        this.permission = permission;
    }

    public boolean check(Player p) {
        if(p.isOp())
            return true;
        if(p.hasPermission(this.permission))
            return true;
        p.sendMessage(Messages.no_permission.language(p).queue());
        return false;
    }
}
