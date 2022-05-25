package me.idbi.hcf.CustomFiles;

import me.idbi.hcf.Main;
import me.idbi.hcf.MessagesEnums.ListMessages;
import me.idbi.hcf.MessagesEnums.Messages;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    private static ConfigManager mng = new ConfigManager();
    private Main m = Main.getPlugin(Main.class);
    public static ConfigManager getManager(){
        return mng;
    }

    public void setup(){

        for (Messages msg : Messages.values()) {
            msg.load();
        }

        for (ListMessages msg : ListMessages.values()) {
            msg.load();

        }

        for (ConfigLibrary msg : ConfigLibrary.values()) {
            msg.load();
        }
    }

    public FileConfiguration getMessages(){
        return MessagesFile.getMessages();
    }

    public FileConfiguration getConfig(){
        return m.getConfig();
    }

    public FileConfiguration getListMessage(){
        return MessagesFile.getMessages();
    }
}
