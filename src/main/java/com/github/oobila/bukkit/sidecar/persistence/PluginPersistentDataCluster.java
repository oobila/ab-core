package com.github.oobila.bukkit.sidecar.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The PluginPersistentDataCluster works the same as PluginPersistentData but can only be used on the Map type.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginPersistentDataCluster {

    String path();

}
