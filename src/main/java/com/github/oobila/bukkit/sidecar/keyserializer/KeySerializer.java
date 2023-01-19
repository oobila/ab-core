package com.github.oobila.bukkit.sidecar.keyserializer;

public interface KeySerializer<T extends Object> {

    String serialize(T object);

    T deserialize(String string);

}
