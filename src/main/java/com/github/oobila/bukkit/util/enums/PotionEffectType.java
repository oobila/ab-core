package com.github.oobila.bukkit.util.enums;

import lombok.Getter;

public enum PotionEffectType {

    SPEED(org.bukkit.potion.PotionEffectType.SPEED),
    SLOW(org.bukkit.potion.PotionEffectType.SLOW),
    FAST_DIGGING(org.bukkit.potion.PotionEffectType.FAST_DIGGING),
    SLOW_DIGGING(org.bukkit.potion.PotionEffectType.SLOW_DIGGING),
    INCREASE_DAMAGE(org.bukkit.potion.PotionEffectType.INCREASE_DAMAGE),
    HEAL(org.bukkit.potion.PotionEffectType.HEAL),
    HARM(org.bukkit.potion.PotionEffectType.HARM),
    JUMP(org.bukkit.potion.PotionEffectType.JUMP),
    CONFUSION(org.bukkit.potion.PotionEffectType.CONFUSION),
    REGENERATION(org.bukkit.potion.PotionEffectType.REGENERATION),
    DAMAGE_RESISTANCE(org.bukkit.potion.PotionEffectType.DAMAGE_RESISTANCE),
    FIRE_RESISTANCE(org.bukkit.potion.PotionEffectType.FIRE_RESISTANCE),
    WATER_BREATHING(org.bukkit.potion.PotionEffectType.WATER_BREATHING),
    INVISIBILITY(org.bukkit.potion.PotionEffectType.INVISIBILITY),
    BLINDNESS(org.bukkit.potion.PotionEffectType.BLINDNESS),
    NIGHT_VISION(org.bukkit.potion.PotionEffectType.NIGHT_VISION),
    HUNGER(org.bukkit.potion.PotionEffectType.HUNGER),
    WEAKNESS(org.bukkit.potion.PotionEffectType.WEAKNESS),
    POISON(org.bukkit.potion.PotionEffectType.POISON),
    WITHER(org.bukkit.potion.PotionEffectType.WITHER),
    HEALTH_BOOST(org.bukkit.potion.PotionEffectType.HEALTH_BOOST),
    ABSORPTION(org.bukkit.potion.PotionEffectType.ABSORPTION),
    SATURATION(org.bukkit.potion.PotionEffectType.SATURATION),
    GLOWING(org.bukkit.potion.PotionEffectType.GLOWING),
    LEVITATION(org.bukkit.potion.PotionEffectType.LEVITATION),
    LUCK(org.bukkit.potion.PotionEffectType.LUCK),
    UNLUCK(org.bukkit.potion.PotionEffectType.UNLUCK),
    SLOW_FALLING(org.bukkit.potion.PotionEffectType.SLOW_FALLING),
    CONDUIT_POWER(org.bukkit.potion.PotionEffectType.CONDUIT_POWER),
    DOLPHINS_GRACE(org.bukkit.potion.PotionEffectType.DOLPHINS_GRACE),
    BAD_OMEN(org.bukkit.potion.PotionEffectType.BAD_OMEN),
    HERO_OF_THE_VILLAGE(org.bukkit.potion.PotionEffectType.HERO_OF_THE_VILLAGE);

    @Getter
    private org.bukkit.potion.PotionEffectType type;

    PotionEffectType(org.bukkit.potion.PotionEffectType type) {
        this.type = type;
    }
}
