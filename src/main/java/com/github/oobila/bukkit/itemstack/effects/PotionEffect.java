package com.github.oobila.bukkit.itemstack.effects;

import com.github.oobila.bukkit.effects.AttributeManager;
import com.github.oobila.bukkit.effects.PlayerEffect;
import com.github.oobila.bukkit.util.ChatColorUtils;
import com.github.oobila.bukkit.util.enums.PotionEffectType;
import lombok.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.List;

@Data
public class PotionEffect implements PlayerEffect {

    private final String name;
    private PotionEffectType effectType;
    private int duration = 3;
    private int amplifier = 1;
    private boolean isAmbient = false;
    private boolean hasParticles = true;
    private boolean hasIcon = true;

    public PotionEffect(String name, PotionEffectType effectType) {
        this.name = name;
        this.effectType = effectType;
        AttributeManager.register(this);
    }

    @Override
    public void playEffect(Player player, Event event) {
        org.bukkit.potion.PotionEffectType potionEffectType = effectType.getType();
        player.addPotionEffects(List.of(
                new org.bukkit.potion.PotionEffect(potionEffectType, duration * 20, amplifier, isAmbient, hasParticles, hasIcon)
        ));
    }

    @Override
    public String getDisplayName() {
        return ChatColorUtils.fromColor(effectType.getType().getColor()) + effectType.name() + " " + amplifier + " " + duration + "s";
    }
}