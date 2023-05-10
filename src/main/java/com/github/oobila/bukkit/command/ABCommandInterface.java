package com.github.oobila.bukkit.command;

import org.bukkit.entity.Player;

import java.util.List;

public interface ABCommandInterface {

    void onCommand(Player player, ABCommand command, String label, String[] args);

    List<String> onTabComplete(Player player, ABCommand command, String label, String[] args);

}