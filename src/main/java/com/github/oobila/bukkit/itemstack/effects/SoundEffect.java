package com.github.oobila.bukkit.itemstack.effects;

import com.github.oobila.bukkit.effects.AttributeManager;
import com.github.oobila.bukkit.effects.PlayerEffect;
import com.github.oobila.bukkit.util.ChatColorUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

@Data
public class SoundEffect implements PlayerEffect {

    private final String name;
    private boolean isPlayerOnly = true;
    private Sound sound;
    private float volume = 1f;
    private float pitch = 1f;

    public SoundEffect(String name, Sound sound) {
        this.name = name;
        this.sound = sound;
        AttributeManager.register(this);
    }

    @Override
    public void playEffect(Player player, Event event) {
        if (isPlayerOnly) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        } else {
            Bukkit.getOnlinePlayers().forEach(p -> p.playSound(player.getLocation(), sound, volume, pitch));
        }
    }

    @Override
    public String getDisplayName() {
        return null;
    }
}
