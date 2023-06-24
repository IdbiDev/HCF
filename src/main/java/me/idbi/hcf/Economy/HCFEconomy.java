package me.idbi.hcf.Economy;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import net.milkbowl.vault.economy.EconomyResponse;

import java.util.List;
import java.util.UUID;

public class HCFEconomy implements Economy {
    private Main m = Main.getInstance();

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "HCFPlus";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double v) {
        return null;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public boolean hasAccount(String s) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public double getBalance(String s) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        return m.playerBank.get(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        UUID uuid = offlinePlayer.getUniqueId();
        return m.playerBank.get(uuid);
    }

    @Override
    public double getBalance(String s, String s1) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        return m.playerBank.get(uuid);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        UUID uuid = offlinePlayer.getUniqueId();
        return m.playerBank.get(uuid);
    }

    @Override
    public boolean has(String s, double v) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(s);
        UUID uuid = offlinePlayer.getUniqueId();
        double oldBalance = m.playerBank.get(uuid);
        return oldBalance >= v;
    }


    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        double oldBalance = m.playerBank.get(uuid);
        return oldBalance >= v;
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(s);
        UUID uuid = offlinePlayer.getUniqueId();
        double oldBalance = m.playerBank.get(uuid);
        m.playerBank.put(uuid, oldBalance - v);
        return new EconomyResponse(v,oldBalance - v, EconomyResponse.ResponseType.SUCCESS,"");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        double oldBalance = m.playerBank.get(uuid);
        m.playerBank.put(uuid, oldBalance - v);
        return new EconomyResponse(v,oldBalance - v, EconomyResponse.ResponseType.SUCCESS,"");
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        double oldBalance = m.playerBank.get(uuid);
        m.playerBank.put(uuid, oldBalance + v);
        return new EconomyResponse(v,oldBalance + v, EconomyResponse.ResponseType.SUCCESS,"");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        double oldBalance = m.playerBank.get(uuid);
        m.playerBank.put(uuid, oldBalance + v);
        return new EconomyResponse(v,oldBalance + v, EconomyResponse.ResponseType.SUCCESS,"");
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        Player player = Bukkit.getPlayer(s);
        UUID uuid = player.getUniqueId();
        double oldBalance = m.playerBank.get(uuid);
        m.playerBank.put(uuid, oldBalance + v);
        return new EconomyResponse(v,oldBalance + v, EconomyResponse.ResponseType.SUCCESS,"");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        UUID uuid = offlinePlayer.getUniqueId();
        double oldBalance = m.playerBank.get(uuid);
        m.playerBank.put(uuid, oldBalance + v);
        return new EconomyResponse(v,oldBalance + v, EconomyResponse.ResponseType.SUCCESS,"");
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        m.playerBank.put(offlinePlayer.getUniqueId(), Config.DefaultBalance.asDouble());
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}
