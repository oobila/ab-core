package com.github.oobila.bukkit.util.skull;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Non-player skull is a player head with a base64 texture string to give it a unique texture
 */
public class NonPlayerSkull extends ItemStack {

    private static final String URL_PREFIX = "http://textures.minecraft.net/texture/";
    private static final Pattern BASE64_PATTERN = Pattern.compile("\\\"http:\\/\\/textures\\.minecraft\\.net\\/texture\\/(.*)\\\"");

    @SuppressWarnings("java:S3011")
    public NonPlayerSkull(String textureId) {
        super(Material.PLAYER_HEAD);

        try {
            if(!textureId.isEmpty()) {
                SkullMeta headMeta = (SkullMeta) getItemMeta();
                setTexture(headMeta, textureId);
                setItemMeta(headMeta);
            }
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.WARNING, "Could not create custom skull", e);
        }
    }

    public static void setTexture(SkullMeta meta, URL skinUrl) {
        PlayerProfile profile = Bukkit.createPlayerProfile("ABCore");
        PlayerTextures textures = profile.getTextures();
        textures.setSkin(skinUrl);
        profile.setTextures(textures);
        meta.setOwnerProfile(profile);
    }

    public static void setTexture(SkullMeta meta, String textureId) {
        try {
            if (textureId.length() > 160) { //base64String
                byte[] decodedBytes = Base64.getDecoder().decode(textureId);
                Matcher matcher = BASE64_PATTERN.matcher(new String(decodedBytes));
                if (matcher.find()) {
                    textureId = matcher.group(1);
                }
            }
            textureId = textureId.replaceAll("[^0-9a-zA-Z]", "");
            setTexture(meta, new URL(URL_PREFIX + textureId));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
