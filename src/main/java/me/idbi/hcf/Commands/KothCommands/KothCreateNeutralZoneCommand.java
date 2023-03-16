package me.idbi.hcf.Commands.KothCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.HCF_Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import static me.idbi.hcf.Koth.Koth.getKothFromName;
import static me.idbi.hcf.Tools.HCF_Claiming.KothPrepare;

public class KothCreateNeutralZoneCommand extends SubCommand {
    @Override
    public String getName() {
        return "createneutralzone";
    }

    @Override
    public boolean isCommand(String argument) {
        return argument.equalsIgnoreCase(getName()) ;
    }

    @Override
    public String getDescription() {
        return "Gives a claiming wand, it can be used to create a zone around the koth. The claim will have a special attribute (PvP, No block break/place/interact),this is NOT a capture zone!";
    }

    @Override
    public String getSyntax() {
        return "/koth " + getName() + " <Koth name>";
    }

    @Override
    public String getPermission() {
        return "factions.commands.koth." + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getPermission());
    }

    @Override
    public boolean hasCooldown(Player p) {
        return false;
    }

    @Override
    public void addCooldown(Player p) {

    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public void perform(Player p, String[] args) {
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if (getKothFromName(args[1]) != 0) {
            if (player.getClaimType() != HCF_Claiming.ClaimTypes.SPECIAL) {
                if (KothPrepare(p)) {
                    player.setClaimType(HCF_Claiming.ClaimTypes.SPECIAL);
                    player.setKothId(getKothFromName(args[1]));
                }
            }
        } else {
            p.sendMessage(Messages.koth_invalid_name.language(p).queue());
        }
    }
}
