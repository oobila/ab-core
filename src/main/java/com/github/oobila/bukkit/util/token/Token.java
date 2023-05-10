package com.github.oobila.bukkit.util.token;

import com.github.oobila.bukkit.util.skull.NonPlayerSkull;
import com.github.oobila.bukkit.util.ItemMetaUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.text.MessageFormat;
import java.util.*;

public class Token<T extends Tokenable> implements ConfigurationSerializable {

    private static final String TOKEN_STRING = "token";
    private static final String DEFAULT_TOKEN_TEXTURE = "5359d91277242fc01c309accb87b533f1929be176ecba2cde63bf635e05e699b";
    private static final String TOKEN_LORE_STRING = ChatColor.BLACK + "" + ChatColor.BOLD + "{0}-" + TOKEN_STRING.toUpperCase();

    private Plugin plugin;

    @Getter
    private String name;

    @Getter
    private UUID id;

    @Getter
    private ItemStack tokenItem;

    @Getter @Setter
    private T object;

    public Token(Plugin plugin, String name, UUID id, String texture, T object) {
        this(
                plugin,
                name,
                id,
                texture == null || texture.isEmpty() ? new NonPlayerSkull(DEFAULT_TOKEN_TEXTURE) : new NonPlayerSkull(texture),
                object);
    }

    public Token(Plugin plugin, String name, UUID id, ItemStack tokenItem, T object) {
        this.plugin = plugin;
        this.name = name;
        this.id = id;
        this.tokenItem = tokenItem;
        this.object = object;
        ItemMeta itemMeta = tokenItem.getItemMeta();
        List<String> lore = itemMeta.getLore();
        if(lore == null){
            lore = new ArrayList<>();
        }
        lore.add(0, MessageFormat.format(TOKEN_LORE_STRING, name.toUpperCase()));
        itemMeta.setLore(lore);
        ItemMetaUtil.addInt(itemMeta, new NamespacedKey(plugin, TOKEN_STRING), 1);
        tokenItem.setItemMeta(itemMeta);

        TokenManager.addToken(this, object);
    }

    public Class<T> getObjectType(){
        return (Class<T>) object.getClass();
    }

    public static boolean isToken(Plugin plugin, ItemStack itemStack){
        return ItemMetaUtil.getInt(itemStack.getItemMeta(), new NamespacedKey(plugin, TOKEN_STRING)) == 1;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("plugin", plugin.getName());
        result.put("name", name);
        result.put("id", id.toString());
        result.put("itemStack", tokenItem);
        return result;
    }

    public static Token deserialize(Map<String, Object> args) {
        return new Token(
                Bukkit.getPluginManager().getPlugin((String) args.get("plugin")),
                (String) args.get("name"),
                UUID.fromString((String) args.get("id")),
                (ItemStack) args.get("itemStack"),
                null
        );
    }
}
