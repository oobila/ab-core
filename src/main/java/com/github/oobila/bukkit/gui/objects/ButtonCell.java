package com.github.oobila.bukkit.gui.objects;

import com.github.oobila.bukkit.gui.Cell;
import com.github.oobila.bukkit.gui.GuiBase;
import com.github.oobila.bukkit.util.text.NotificationManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class ButtonCell extends MenuItemCell {

    private ButtonClickAction buttonClickAction;
    private long cooldownSeconds;
    private ZonedDateTime dateTime;

    public ButtonCell(ItemStack itemStack, ButtonClickAction buttonClickAction) {
        super(itemStack);
        this.buttonClickAction = buttonClickAction;
    }

    @Override
    public void onClick(InventoryClickEvent e, Player player, Cell cell, GuiBase guiMenu) {
        ZonedDateTime now = ZonedDateTime.now();
        if (dateTime == null || cooldownSeconds <= 0 ||
                (dateTime.plus(cooldownSeconds, ChronoUnit.SECONDS).isBefore(now))) {
            buttonClickAction.onButtonClick(e, player, (ButtonCell) cell, guiMenu);
            dateTime = now;
        } else {
            NotificationManager.sendNotification(player, MessageFormat.format(
                    "This button has a {0} second cooldown", cooldownSeconds));
        }
        e.setCancelled(true);
    }

    public void addCooldown(long seconds) {
        this.cooldownSeconds = seconds;
    }

    public interface ButtonClickAction {
        void onButtonClick(InventoryClickEvent e, Player player, ButtonCell button, GuiBase guiMenu);
    }
}
