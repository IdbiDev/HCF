package me.idbi.hcf.tools;

import me.idbi.hcf.Main;

import java.sql.Connection;

public class SQL_Generator {
    private static final Connection con = Main.getConnection("SQL_GENERATOR");

    public SQL_Generator() {
        String[] tables = {
                """
                    CREATE TABLE IF NOT EXISTS `claims` (
                      `ID` bigint(255) NOT NULL AUTO_INCREMENT,
                      `factionid` int(255) NOT NULL DEFAULT 0,
                      `startX` int(255) NOT NULL DEFAULT 0,
                      `startZ` int(255) NOT NULL DEFAULT 0,
                      `endX` int(255) NOT NULL DEFAULT 0,
                      `endZ` int(255) NOT NULL DEFAULT 0,
                      `type` text NOT NULL DEFAULT 'faction' COMMENT 'Types: faction;spawn;koth;end;',
                      PRIMARY KEY (`ID`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """,
                """
                    CREATE TABLE IF NOT EXISTS `deathbans` (
                      `ID` int(255) NOT NULL AUTO_INCREMENT,
                      `uuid` text NOT NULL,
                      `time` bigint(255) NOT NULL DEFAULT 0,
                      PRIMARY KEY (`ID`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """,
                """
                    CREATE TABLE IF NOT EXISTS `factions` (
                      `ID` int(255) NOT NULL AUTO_INCREMENT,
                      `name` varchar(255) NOT NULL,
                      `money` int(255) NOT NULL DEFAULT 0,
                      `home` text CHARACTER SET utf8 COLLATE utf8_hungarian_ci DEFAULT NULL,
                      `DTR` int(11) NOT NULL DEFAULT 3,
                      `leader` text NOT NULL,
                      PRIMARY KEY (`ID`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """,
                """
                    CREATE TABLE IF NOT EXISTS `members` (
                      `ID` int(255) NOT NULL AUTO_INCREMENT,
                      `name` varchar(255) NOT NULL,
                      `faction` int(255) NOT NULL DEFAULT 0,
                      `rank` varchar(255) CHARACTER SET utf8 COLLATE utf8_hungarian_ci DEFAULT 'alap',
                      `kills` int(255) NOT NULL DEFAULT 0,
                      `deaths` int(255) NOT NULL DEFAULT 0,
                      `money` int(255) NOT NULL DEFAULT 5000,
                      `uuid` text DEFAULT NULL,
                      `online` int(2) NOT NULL DEFAULT 0,
                      `factionname` varchar(255) NOT NULL DEFAULT 'Nincs',
                      PRIMARY KEY (`ID`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """,
                """
                    CREATE TABLE IF NOT EXISTS `ranks` (
                      `ID` int(11) NOT NULL AUTO_INCREMENT,
                      `faction` int(11) NOT NULL,
                      `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_hungarian_ci NOT NULL,
                      `permissions` varchar(255) NOT NULL DEFAULT '{"0":true}',
                      `isDefault` int(2) NOT NULL DEFAULT 0,
                      `isLeader` int(2) NOT NULL DEFAULT 0,
                      PRIMARY KEY (`ID`)
                    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """,
                """
                    INSERT IGNORE INTO `factions` SET ID = '-1', money = 0, name = 'spawn', leader = '';
                """
        };
        for (String data : tables) {
            SQL_Connection.dbExecute(con, data);
        }
    }


}
