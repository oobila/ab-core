package com.github.oobila.bukkit.itemstack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.oobila.bukkit.CorePlugin;
import com.github.oobila.bukkit.effects.AttributeManager;
import com.github.oobila.bukkit.effects.Effect;
import com.github.oobila.bukkit.effects.Attribute;
import com.github.oobila.bukkit.itemstack.effects.ItemBehaviour;
import com.github.oobila.bukkit.itemstack.effects.ItemSlot;
import com.github.oobila.bukkit.util.ItemMetaUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CustomItemStack extends ItemStack {

    //effect_uses - get rid of?
    //effect_type - add
    //effect_behaviour - good
    //last_used - good
    //item_use_amount - get rid of?
    //item_behaviours_slot - rename?
    //effect_cooldown - good
    //effect_level - add

    private static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String CUSTOM_LORE_SECTION_DELIM = "//";
    private static final String CUSTOM_LORE_SECTION_DELIM_REPLACEMENT = "/ /";

    public CustomItemStack(Material material){
        super(material);
    }

    public CustomItemStack(ItemStack itemStack){
        super(itemStack.getType());
        setItemMeta(itemStack.getItemMeta());
        setAmount(itemStack.getAmount());
        setData(itemStack.getData());
    }

    protected void setDisplayName(String displayName){
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(displayName);
        setItemMeta(meta);
    }

    public void addMeta(String name, String value) {
        ItemMeta meta = getItemMeta();
        ItemMetaUtil.addString(meta, new NamespacedKey(CorePlugin.getInstance(), name), value);
        setItemMeta(meta);
    }

    public void addMeta(String name, UUID value) {
        ItemMeta meta = getItemMeta();
        ItemMetaUtil.addUUID(meta, new NamespacedKey(CorePlugin.getInstance(), name), value);
        setItemMeta(meta);
    }

    public void addMeta(String name, int value) {
        ItemMeta meta = getItemMeta();
        ItemMetaUtil.addInt(meta, new NamespacedKey(CorePlugin.getInstance(), name), value);
        setItemMeta(meta);
    }

    public void addMeta(String name, double value) {
        ItemMeta meta = getItemMeta();
        ItemMetaUtil.addDouble(meta, new NamespacedKey(CorePlugin.getInstance(), name), value);
        setItemMeta(meta);
    }

    public void addMeta(String name, LocalDateTime value) {
        ItemMeta meta = getItemMeta();
        ItemMetaUtil.addLocalDateTime(meta, new NamespacedKey(CorePlugin.getInstance(), name), value);
        setItemMeta(meta);
    }

    public String getMetaString(String name) {
        return ItemMetaUtil.getString(getItemMeta(), new NamespacedKey(CorePlugin.getInstance(), name));
    }

    public UUID getMetaUUID(String name) {
        return ItemMetaUtil.getUUID(getItemMeta(), new NamespacedKey(CorePlugin.getInstance(), name));
    }

    public int getMetaInt(String name) {
        return ItemMetaUtil.getInt(getItemMeta(), new NamespacedKey(CorePlugin.getInstance(), name));
    }

    public double getMetaDouble(String name) {
        return ItemMetaUtil.getDouble(getItemMeta(), new NamespacedKey(CorePlugin.getInstance(), name));
    }

    public LocalDateTime getMetaDate(String name) {
        return ItemMetaUtil.getLocalDateTime(getItemMeta(), new NamespacedKey(CorePlugin.getInstance(), name));
    }

    public void removeMeta(String name){
        ItemMeta meta = getItemMeta();
        ItemMetaUtil.remove(meta, new NamespacedKey(CorePlugin.getInstance(), name));
        setItemMeta(meta);
    }

    public void setLore(List<String> lore) {
        removeMeta("lore");
        if (!lore.isEmpty()) {
            addMeta("lore", lore.stream()
                    .map(s -> s.replace(CUSTOM_LORE_SECTION_DELIM, CUSTOM_LORE_SECTION_DELIM_REPLACEMENT))
                    .collect(Collectors.joining(CUSTOM_LORE_SECTION_DELIM))
            );
        }
        updateLore();
    }

    public List<String> getLore() {
        String loreString = getMetaString("lore");
        if (loreString == null) {
            return null;
        }
        return Arrays.stream(loreString.split(CUSTOM_LORE_SECTION_DELIM)).collect(Collectors.toList());
    }

    private void updateLore() {
        List<String> currentLore = getLore();
        if (currentLore == null) {
            currentLore = new ArrayList<>();
        }
        final List<String> lore = currentLore;

        getItemEffects().stream()
                .filter(effect -> effect.getDisplayName() != null && !effect.getDisplayName().isEmpty())
                .forEach(effect -> lore.add(effect.getDisplayName()));
        getAttributes().stream()
                .filter(attribute -> attribute.getDisplayName() != null && !attribute.getDisplayName().isEmpty())
                .forEach(attribute -> lore.add(attribute.getDisplayName()));

        ItemMeta meta = getItemMeta();
        meta.setLore(lore);
        setItemMeta(meta);
    }

    public void addItemEffect(Effect effect) {
        if (AttributeManager.effectOf(effect.getName()) == null) {
            Bukkit.getLogger().warning("Effect \"" + effect.getName() + "\" has not been registered!");
            return;
        }
        Set<Effect> effects = new HashSet<>(getItemEffects());
        effects.add(effect);
        String effectString = effects.stream()
                .map(Effect::getName)
                .collect(Collectors.joining("|"));
        removeMeta("itemEffects");
        addMeta("itemEffects", effectString);
        updateLore();
    }

    public Set<Effect> getItemEffects() {
        String effectString = getMetaString("itemEffects");
        if (effectString == null) {
            return Collections.EMPTY_SET;
        }
        return Arrays.stream(effectString.split("\\|"))
                .map(AttributeManager::effectOf)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void addAttribute(Attribute attribute) {
        if (AttributeManager.attributeOf(attribute.getName()) == null) {
            Bukkit.getLogger().warning("Attribute \"" + attribute.getName() + "\" has not been registered!");
            return;
        }
        Set<Attribute> attributes = new HashSet<>(getAttributes());
        attributes.add(attribute);
        String attributeString = attributes.stream()
                .map(Attribute::getName)
                .collect(Collectors.joining("|"));
        removeMeta("itemAttributes");
        addMeta("itemAttributes", attributeString);
        updateLore();
    }

    public Set<Attribute> getAttributes() {
        String attributeString = getMetaString("itemAttributes");
        if (attributeString == null) {
            return Collections.EMPTY_SET;
        }
        return Arrays.stream(attributeString.split("\\|"))
                .map(AttributeManager::attributeOf)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public void setBehaviour(ItemBehaviour itemBehaviour, ItemSlot itemSlot) {
        removeMeta("itemBehaviour");
        addMeta("itemBehaviour", itemBehaviour.toString());
        removeMeta("itemBehaviourSlot");
        addMeta("itemBehaviourSlot", itemSlot.toString());
    }

    public ItemBehaviour getBehaviour() {
        return ItemBehaviour.valueOf(getMetaString("itemBehaviour"));
    }

    public ItemSlot getBehaviourSlot() {
        return ItemSlot.valueOf(getMetaString("itemBehaviourSlot"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
