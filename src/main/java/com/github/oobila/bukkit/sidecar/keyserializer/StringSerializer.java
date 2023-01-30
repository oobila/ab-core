package com.github.oobila.bukkit.sidecar.keyserializer;

public class StringSerializer implements KeySerializer<String> {

    @Override
    public String serialize(String object) {
        return object;
    }

    @Override
    public String deserialize(String string) {
        return string;
    }
}
