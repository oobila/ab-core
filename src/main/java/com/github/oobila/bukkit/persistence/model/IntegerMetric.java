package com.github.oobila.bukkit.persistence.model;

import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

public class IntegerMetric extends PersistedObject {

    @Getter
    private int value;

    public IntegerMetric() {
        super(ZonedDateTime.now());
        this.value = 0;
    }

    public IntegerMetric(int value) {
        super(ZonedDateTime.now());
        this.value = value;
    }

    public int incrementAndGet() {
        value++;
        return value;
    }

    public int incrementAndGet(int amount) {
        value += amount;
        return value;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("", value);
        return map;
    }

    public static IntegerMetric deserialize(Map<String, Object> args) {
        return new IntegerMetric((Integer) args.get(""));
    }

}
