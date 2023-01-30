package com.github.oobila.bukkit.sidecar.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The PluginPersistentData annotation is to be used on an object which implements ConfigSerializable and the public
 * static method: deserialize(Map<String, Object> args). The PluginPersistentData annotation can also be used on Map,
 * Set and List.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginPersistentData {

    String path();

}
