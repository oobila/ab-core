package com.github.oobila.bukkit.command;

import org.bukkit.entity.Player;

public interface PlayerCommandExecutor {

    void onCommand(Player sender, ABCommand command, String label, String[] args);

}
