package me.idbi.hcf.CustomFiles.Configs;

public class ConfigComments {

    private static ConfigComments comments;

    public ConfigComments() {
        comments = this;
    }

    public static ConfigComments get() {
        return comments;
    }

}
