package me.idbi.hcf.Commands.FactionCommands;

import me.idbi.hcf.Commands.SubCommand;
import me.idbi.hcf.CustomFiles.Messages.Messages;
import me.idbi.hcf.Tools.Claiming;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class FactionUnclaimCommand extends SubCommand {

    @Override
    public String getName() {
        return "unclaim";
    }

    @Override
    public boolean isCommand(String argument) {
            return argument.equalsIgnoreCase(getName());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/faction " + getName();
    }

    @Override
    public boolean hasPermission(Player p) {
        return p.hasPermission(getName());
    }

    @Override
    public boolean hasCooldown(Player p) {
        if(!SubCommand.commandCooldowns.get(this).containsKey(p)) return false;
        return SubCommand.commandCooldowns.get(this).get(p) > System.currentTimeMillis();
    }

    @Override
    public void addCooldown(Player p) {
        HashMap<Player, Long> hashMap = SubCommand.commandCooldowns.get(this);
        hashMap.put(p, System.currentTimeMillis() + (getCooldown() * 1000L));
        SubCommand.commandCooldowns.put(this, hashMap);
    }

    @Override
    public int getCooldown() {
        return 2;
    }

    @Override
    public void perform(Player p, String[] args) {
        addCooldown(p);
        HCFPlayer player = HCFPlayer.getPlayer(p);
        if(player.inFaction()) {
            if(player.getRank().isLeader()) {
                if(!player.getFaction().getClaims().isEmpty()) {
                    double backmoney = Claiming.calculateMoneyFromClaim(player.getFaction());
                    player.addMoney(Math.toIntExact(Math.round(backmoney)));
                    player.getFaction().clearClaims(); // done
                    Messages.success_unclaim.language(p).queue();

                }else {
                    p.sendMessage(Messages.faction_not_have_claim.language(p).queue());
                }
            }else {
                p.sendMessage(Messages.no_permission.language(p).queue());
            }
        }else {
            p.sendMessage(Messages.not_in_faction.language(p).queue());
        }
    }
}
