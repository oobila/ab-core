package com.github.oobila.bukkit.effects;

import org.bukkit.event.Event;

public interface Effect<T> extends Attribute{

    void playEffect(T t, Event event);

}
