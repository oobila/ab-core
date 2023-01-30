package com.github.oobila.bukkit.sidecar.keyserializer;

import java.util.UUID;

public class UUIDSerializer implements KeySerializer<UUID> {

    @Override
    public String serialize(UUID object) {
        return object.toString();
    }

    @Override
    public UUID deserialize(String string) {
        return UUID.fromString(string);
    }
}
