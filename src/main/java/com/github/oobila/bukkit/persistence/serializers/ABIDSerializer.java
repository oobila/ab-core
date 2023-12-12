package com.github.oobila.bukkit.persistence.serializers;

import com.github.oobila.bukkit.identity.ABID;

public class ABIDSerializer implements KeySerializer<ABID> {

    @Override
    public String serialize(ABID object) {
        return object.toString();
    }

    @Override
    public ABID deserialize(String string) {
        return ABID.fromString(string);
    }
}
