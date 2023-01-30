package com.github.oobila.bukkit.command;

import org.bukkit.entity.Player;

import java.util.List;

public interface SuggestionCallable<T> {

    List<T> getSuggestions(Player player);

}
