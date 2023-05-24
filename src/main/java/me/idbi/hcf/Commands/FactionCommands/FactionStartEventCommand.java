package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Main;
import me.idbi.hcf.WorldModes.Deathmatch;
import me.idbi.hcf.WorldModes.EOTW;
import me.idbi.hcf.WorldModes.SOTW;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.util.HashMap;

public class FactionStartEventCommand extends SubCommand {
    public static Connection con = Main.getConnection();
    SOTW sotw = new SOTW();
    EOTW eotw = new EOTW();
    Deathmatch deathmatch = new Deathmatch();


    @Override
    public String getName() {
        return "event";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return "Starts the selected event.";
    }

    @Override
    public String getSyntax() {
        return "/faction event <start | stop> <sotw | eotw | deathmatch>";
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        if (args[1].equalsIgnoreCase("start")) {
            if (args[2].equalsIgnoreCase("sotw")) {
                this.sotw.enable();
                p.sendMessage(Messages.enable_sotw.language(p).queue());
            } else if (args[2].equalsIgnoreCase("eotw")) {
                this.eotw.enable();
                p.sendMessage(Messages.enable_eotw.language(p).queue());
            } else if (args[2].equalsIgnoreCase("deathmatch")) {
                this.deathmatch.enable();
                p.sendMessage(Messages.enable_deathmatch.language(p).queue());
            }
        } else if (args[1].equalsIgnoreCase("stop")) {
            if (args[2].equalsIgnoreCase("sotw")) {
                this.sotw.disable();
                p.sendMessage(Messages.disable_sotw.language(p).queue());
            } else if (args[2].equalsIgnoreCase("eotw")) {
                this.eotw.disable();
                p.sendMessage(Messages.disable_eotw.language(p).queue());
            } else if (args[2].equalsIgnoreCase("deathmatch")) {
                this.deathmatch.disable();
                p.sendMessage(Messages.disable_deathmatch.language(p).queue());
            }
        }
    }
}
