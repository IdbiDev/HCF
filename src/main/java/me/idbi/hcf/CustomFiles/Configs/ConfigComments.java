package me.idbi.hcf.CustomFiles.Configs;

public class ConfigComments {

    private static ConfigComments comments;

    public ConfigComments() {
        comments = this;
    }

    public static ConfigComments get() {
        return comments;
    }

    protected String[] getEnderPearlCooldown() {
        return new String[]{"Sets the enderpearl cooldown, the value is must be in seconds!", "Buzi az adbi"};
    }

    protected String[] getSQL() {
        return new String[]{"Sets the SQL "};
    }

    protected String[] getSQLPort() {
        return new String[]{"Általában ez 3306"};
    }
}
