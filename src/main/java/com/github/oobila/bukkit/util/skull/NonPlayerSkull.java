package com.github.oobila.bukkit.util.skull;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Material;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.checkerframework.checker.units.qual.A;

import java.lang.reflect.Field;
import java.util.UUID;

/**
 * Non-player skull is a player head with a base64 texture string to give it a unique texture
 */
public class NonPlayerSkull extends ItemStack {

    @SuppressWarnings("java:S3011")
    public NonPlayerSkull(String base64String) {
        super(Material.PLAYER_HEAD);
        if(!base64String.isEmpty()) {
            SkullMeta headMeta = (SkullMeta) getItemMeta();
            setTexture(headMeta, base64String);
            setItemMeta(headMeta);
        }
    }

    public static void setTexture(SkullMeta headMeta, String base64String) {
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(base64String.getBytes()), null);
        profile.getProperties().put("textures", new Property("textures", base64String));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e1) {
            e1.printStackTrace();
        }
    }
}
