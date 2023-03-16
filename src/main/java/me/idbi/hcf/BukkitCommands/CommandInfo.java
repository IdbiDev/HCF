package me.idbi.hcf.BukkitCommands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandInfo {

    String name();
    String description();
    String permission() default ""; //If not specified anyone can use this command
    String syntax() default ""; //A description of what arguments this command takes

}
