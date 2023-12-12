package com.github.oobila.bukkit.persistence.serializers;

public interface ObjectSerializer<T> {

    String serialize(T object);

    T deserialize(String string);

}
