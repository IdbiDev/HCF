package me.idbi.hcf.Tools;

import me.idbi.hcf.CustomFiles.Configs.Config;
import me.idbi.hcf.Main;
import me.idbi.hcf.Tools.Objects.PlayerStatistic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;

public class SQL_Generator {
    private static final Connection con = Main.getConnection();
@SuppressWarnings("unchecked")
    public SQL_Generator() {
        String[] tables = {
                "CREATE TABLE IF NOT EXISTS `claims` (`ID` bigint(255) NOT NULL AUTO_INCREMENT,`factionid` int(255) NOT NULL DEFAULT 0,`startX` int(255) NOT NULL DEFAULT 0,`startZ` int(255) NOT NULL DEFAULT 0,`endX` int(255) NOT NULL DEFAULT 0,`endZ` int(255) NOT NULL DEFAULT 0,`type` varchar(255) NOT NULL DEFAULT 'normal' COMMENT 'Types: normal;protected;koth;end;',`world` varchar(255) NOT NULL DEFAULT 'world', PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ",
                "CREATE TABLE IF NOT EXISTS `factions` (`ID` int(255) NOT NULL AUTO_INCREMENT,`name` varchar(255) NOT NULL,`money` bigint(255) NOT NULL DEFAULT 0,`home` varchar(255) CHARACTER SET utf8 COLLATE utf8_hungarian_ci DEFAULT NULL,`leader` varchar(255) NOT NULL,`points` int(255) NOT NULL DEFAULT 0,`statistics` longtext DEFAULT NULL,`Allies` longtext DEFAULT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci; ",
                "CREATE TABLE IF NOT EXISTS `members` (`ID` int(255) NOT NULL AUTO_INCREMENT,`name` varchar(255) NOT NULL,`faction` int(255) NOT NULL DEFAULT 0,`rank` varchar(255) CHARACTER SET utf8 COLLATE utf8_hungarian_ci DEFAULT 'None',`kills` int(255) NOT NULL DEFAULT 0,`deaths` int(255) NOT NULL DEFAULT 0,`money` int(255) NOT NULL DEFAULT 0,`lives` int(255) NOT NULL DEFAULT 0,`uuid` varchar(255) DEFAULT NULL,`language` varchar(255) NOT NULL DEFAULT '%default_language%',`statistics` longtext DEFAULT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;".replace("%default_language%", Config.DefaultLanguage.asStr()),
                " CREATE TABLE IF NOT EXISTS `ranks` `ID` int(11) NOT NULL AUTO_INCREMENT,`faction` int(11) NOT NULL,`name` varchar(255) CHARACTER SET utf8 COLLATE utf8_hungarian_ci NOT NULL,`isDefault` tinyint(1) NOT NULL DEFAULT 0,`isLeader` tinyint(1) NOT NULL DEFAULT 0,`ALL_Permission` tinyint(1) NOT NULL DEFAULT 0,`MONEY_Permission` tinyint(1) NOT NULL DEFAULT 1,`INVITE_Permission` tinyint(1) NOT NULL DEFAULT 0,`RANK_Permission` tinyint(1) NOT NULL DEFAULT 0,`PLAYER_Permission` tinyint(1) NOT NULL DEFAULT 0,`KICK_Permission` tinyint(1) NOT NULL DEFAULT 0,`priority` int(11) NOT NULL DEFAULT 0, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ",
                " CREATE TABLE IF NOT EXISTS `deathbans` `ID` int(11) NOT NULL AUTO_INCREMENT,`uuid` varchar(255) NOT NULL,`time` varchar(255) NOT NULL, PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ",
//                """
//                    CREATE TABLE IF NOT EXISTS `playerstatistics` (
//                      `ID` int(255) NOT NULL AUTO_INCREMENT,
//                      `uuid` varchar(255) NOT NULL,
//                      `statistics` varchar(255) NOT NULL DEFAULT '{"totalFactions":0,"lastLogin":0,"TimePlayed":0,"MoneySpend":0,"ClassTimes":{"Miner":0,"Archer":0,"Assassin":0,"Total":0,"Bard":0},"MoneyEarned":0,"FactionHistory":[],"startDate":0}',
//                      PRIMARY KEY (`ID`)
//                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
//                """,
                "CREATE TABLE IF NOT EXISTS `logs` (`ID` bigint(255) NOT NULL AUTO_INCREMENT,`player` varchar(255) NOT NULL,`action` varchar(255) NOT NULL,`type` varchar(255) NOT NULL,`time` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(), PRIMARY KEY (`ID`)) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; ",
                "CREATE TRIGGER faction_inserter BEFORE INSERT ON `factions` FOR EACH ROW SET NEW.`Allies` = CASE WHEN NEW.Allies IS NULL THEN '{}' END; ",
                "CREATE TRIGGER member_inserter BEFORE INSERT ON `members` FOR EACH ROW SET NEW.`statistics` = CASE WHEN NEW.statistics IS NULL THEN '%player_statistics%' END; ".replace("%player_statistics%", PlayerStatistic.defaultStats),//                              `statistics` longtext NOT NULL DEFAULT '%player_statistics%',
                "INSERT IGNORE INTO `factions` SET ID = 1, money = 0, name = 'Spawn', leader = '';",
                "INSERT IGNORE INTO `factions` SET ID = 2, money = 0, name = 'Warzone', leader = '';"
        };
        for (String data : tables) {
            PreparedStatement st = null;
            try {
                st = con.prepareStatement(data);
                st.executeUpdate();
                st.close();
            } catch (SQLWarning ignored) {
            }
            catch (SQLException ignored) {
            }

            //SQL_Connection.dbSyncExec(con, data);
        }
    }


}
