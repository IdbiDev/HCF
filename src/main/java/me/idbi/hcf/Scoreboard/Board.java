package me.idbi.hcf.Scoreboard;

import lombok.Getter;
import lombok.Setter;
import me.idbi.hcf.Classes.SubClasses.Miner;
import me.idbi.hcf.CustomFiles.BoardFile;
import me.idbi.hcf.Koth.Koth;
import me.idbi.hcf.Main;
import me.idbi.hcf.Scoreboard.FastBoard.FastBoard;
import me.idbi.hcf.Tools.AdminTools;
import me.idbi.hcf.Tools.Formatter;
import me.idbi.hcf.Tools.Objects.CustomTimers;
import me.idbi.hcf.Tools.Objects.Faction;
import me.idbi.hcf.Tools.Objects.HCFPlayer;
import me.idbi.hcf.Tools.Playertools;
import me.idbi.hcf.Tools.Timers;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class Board {

    private final Player player;
    private long titleChanger;
    private FastBoard fastBoard;
    private int titleIndex;
    private String title;

    public Board(Player player) {
        this.player = player;
        this.titleChanger = System.currentTimeMillis();
        this.titleIndex = 0;
        this.fastBoard = new FastBoard(player);
        this.title = BoardManager.get().getTitle();
    }

    private void tickTitle() {
        if (BoardManager.get().isTitleChanger()) {
            if (this.titleChanger < System.currentTimeMillis()) {
                this.titleChanger = System.currentTimeMillis() + BoardManager.get().getTitleChangerTicks();
                this.fastBoard.updateTitle(this.getTitle());
            }
        }
        else {
            if (!this.fastBoard.getTitle().equals(this.title)) {
                this.fastBoard.updateTitle(this.title);
            }
        }
    }

    public String getTitle() {
        List<String> lines = BoardManager.get().getTitleChangeLines();
        if(this.titleIndex == lines.size()) {
            this.titleIndex = 0;
            return lines.get(0);
        }

        String s = lines.get(this.titleIndex);
        this.titleIndex++;
        return s;
    }


    public void update() {
        if(player == null) {
            this.fastBoard.delete();
            return;
        }
        List<String> lines = getLines();
        if (lines == null || lines.isEmpty()) {
            if (!this.fastBoard.isDeleted()) {
                this.fastBoard.delete();
            }
            return;
        }
        if (this.fastBoard.isDeleted()) {
            this.fastBoard = new FastBoard(this.player);
        }
        this.tickTitle();
        this.fastBoard.updateLines(getLines());
    }

    public List<String> getLines() {
        if(player == null) {
            this.fastBoard.delete();
            return new ArrayList<>();
        }
        List<String> boardList = new ArrayList<>();
        HCFPlayer hcfPlayer = HCFPlayer.getPlayer(player);

        if(hcfPlayer.isDeathBanned()) {
            boardList.add(BoardManager.get().getLine());
            boardList.add(BoardManager.get().getDeathbanInfoTime() + new SimpleDateFormat("HH:mm").format(new Date(hcfPlayer.getDeathTime())));
            boardList.add(BoardManager.get().getDeathbanInfoLives() + hcfPlayer.getLives());
            boardList.add(BoardManager.get().getLine());
            return boardList;
        }
        boardList.add(BoardManager.get().getLine());

        if(hcfPlayer.isInDuty()) {
            List<String> list = new ArrayList<>(BoardManager.get().getStaffModeLines());
            list.replaceAll(s -> s
                    .replace("%vanished%", Playertools.upperFirst(AdminTools.InvisibleManager.getInvisedAdmins().contains(this.player.getUniqueId()) + ""))
                    .replace("%players%", Bukkit.getOnlinePlayers().size() + "")
                    .replace("%staff%", Playertools.getStaffs().size() + "")
                    .replace("%chat%", Playertools.upperFirst(hcfPlayer.getChatType().name()))
                    .replace("%tps%", "21"));
            boardList.addAll(list);
        }

        if(BoardManager.get().getFaction() != null) {
            if(hcfPlayer.inFaction())
                boardList.add(BoardManager.get().getFaction() + hcfPlayer.getFaction().getName());
        }

        if(BoardManager.get().getLocation() != null) {
            boardList.add(BoardManager.get().getLocation() + hcfPlayer.getLocationFormatted());
        }

        // ToDo: Koth
        if(BoardManager.get().getKoth() != null) {
            if(Koth.GLOBAL_AREA != null) {
                boardList.add(BoardManager.get().getKoth().replace("%koth%", Koth.GLOBAL_AREA.getFaction().getName()) + Formatter.formatMMSS(Koth.GLOBAL_TIME * 1000));
            }
        }

        if(BoardManager.get().getActiveClass() != null) {
            boardList.add(BoardManager.get().getActiveClass() + Playertools.upperFirst(hcfPlayer.getPlayerClass().name()));
            switch (hcfPlayer.getPlayerClass()) {
                case BARD:
                    boardList.add(BoardManager.get().getBardEnergy() + Formatter.formatBardEnergy(hcfPlayer.getBardEnergy()));
                    if (Timers.BARD_COOLDOWN.has(hcfPlayer))
                        boardList.add(BoardManager.get().getBardCooldown() + Formatter.getRemaining(Timers.BARD_COOLDOWN.get(hcfPlayer), true));
                    break;
                case MINER:
                    int diamonds = 0;
                    if(Miner.diamonds.containsKey(this.player))
                        diamonds = Miner.diamonds.get(this.player);
                    boardList.add(BoardManager.get().getMinerInvis() + Playertools.upperFirst(this.player.hasPotionEffect(PotionEffectType.INVISIBILITY) + ""));
                    boardList.add(BoardManager.get().getMinerDiamonds() + diamonds + "");
                    break;
            }
        }

        // Player Timers
        for (Timers value : Timers.values()) {
            if(value.has(hcfPlayer)) {
                try {
                    String getLine = ChatColor.translateAlternateColorCodes('&',
                            BoardFile.get().getString("player_timers." + value.name().toLowerCase()));
                    //System.out.println(getLine);
                    if (getLine == null) continue;
                    boardList.add(getLine + Formatter.getRemaining(value.get(hcfPlayer), true));
                } catch (NullPointerException e) {
                    //e.printStackTrace();
                }
            }
        }

        for (CustomTimers value : Main.customSBTimers.values()) {
            boardList.add(BoardManager.get().getCustomTimer().replace("%displayName%", value.text) + Formatter.formatMMSS(value.getTime()));
        }

        if(hcfPlayer.inFaction()) {
            if(BoardManager.get().isTeamFocusEnabled()) {
                if (hcfPlayer.getFaction().getFocusedTeam() != null) {
                    Faction focusedTeam = hcfPlayer.getFaction().getFocusedTeam();
                    List<String> lines = new ArrayList<>(BoardManager.get().getTeamFocusLines());
                    lines.replaceAll(line -> line
                            .replace("%team%", focusedTeam.getName())
                            .replace("%home%", focusedTeam.getFormattedHomeLocationWithoutY())
                            .replace("%dtr%", Formatter.formatDtr(focusedTeam))
                            .replace("%online%", focusedTeam.getOnlineMembers().size() + ""));
                    boardList.addAll(lines);
                }
            }

            if(BoardManager.get().isRallyEnabled()) {
                if(hcfPlayer.getFaction().getRallyPosition() != null) {
                    Location loc = hcfPlayer.getFaction().getRallyPosition();
                    List<String> lines = new ArrayList<>(BoardManager.get().getRallyLines());
                    lines.replaceAll(line -> line
                            .replace("%world%", Playertools.upperFirst(loc.getWorld().getName()))
                            .replace("%location%", Playertools.formatLocation(loc)));
                    boardList.addAll(lines);
                }
            }
        }

        // ToDo: Footer
        if(BoardManager.get().isFooterEnabled()) {
            boardList.addAll(BoardManager.get().getFooterLines());
        }

        List<String> returnList = new ArrayList<>();

        if(boardList.size() > 15) {
            boardList = boardList.subList(0, 15);
            boardList.set(boardList.size() - 1, BoardManager.get().getLine());
        } else if(boardList.size() == 15) {
            boardList.set(boardList.size() - 1, BoardManager.get().getLine());
        } else {
            boardList.add(BoardManager.get().getLine());
        }

        for (String s : boardList) {
            if (s.length() > 30)
                returnList.add(ChatColor.translateAlternateColorCodes('&', s.substring(0, 30)));
            else
                returnList.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return returnList;
    }
}
