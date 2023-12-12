package com.github.oobila.bukkit.effects;

import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class AttributeManager {

    @Getter
    private static final Map<String, Attribute> attributes = new HashMap<>();

    @Getter
    private static final Map<String, Effect<?>> effects = new HashMap<>();


    public static void register(Attribute attribute) {
        if (attributes.containsKey(attribute.getName())) {
            Bukkit.getLogger().warning("Attribute \"" + attribute.getName() + "\" already exists!");
            return;
        }
        attributes.put(attribute.getName(), attribute);
    }

    public static void register(Effect effect) {
        if (effects.containsKey(effect.getName())) {
            Bukkit.getLogger().warning("Effect \"" + effect.getName() + "\" already exists!");
            return;
        }
        effects.put(effect.getName(), effect);
    }

    public static void remove(Attribute attribute) {
        attributes.remove(attribute.getName());
    }

    public static void remove(Effect effect) {
        effects.remove(effect.getName());
    }

    public static Attribute attributeOf(String name) {
        return attributes.get(name);
    }

    public static Effect<?> effectOf(String name) {
        return effects.get(name);
    }
}