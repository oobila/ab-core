package com.github.oobila.bukkit.itemstack;

import com.github.oobila.bukkit.effects.BlockEffect;
import com.github.oobila.bukkit.effects.Effect;
import com.github.oobila.bukkit.effects.LocationEffect;
import com.github.oobila.bukkit.effects.PlayerEffect;
import com.github.oobila.bukkit.itemstack.effects.EffectTags;
import com.github.oobila.bukkit.itemstack.effects.ItemBehaviour;
import com.github.oobila.bukkit.itemstack.effects.ItemSlot;
import com.github.oobila.bukkit.util.WorldUtils;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.logging.Level;

public class CustomItemStackListener implements Listener {

    @EventHandler
    public void onItemUse(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) {
            block = WorldUtils.getBlockAtDistance(event.getPlayer(), 20,true);
        }
        handleItem(
                event,
                event.getPlayer(),
                event.getClickedBlock() == null ? event.getPlayer().getLocation() : event.getClickedBlock().getLocation(),
                block,
                event.getPlayer().getInventory().getItemInMainHand(),
                ItemBehaviour.fromAction(event.getAction()),
                ItemSlot.MAINHAND
        );
        handleItem(
                event,
                event.getPlayer(),
                event.getClickedBlock() == null ? event.getPlayer().getLocation() : event.getClickedBlock().getLocation(),
                block,
                event.getPlayer().getInventory().getItemInOffHand(),
                ItemBehaviour.fromAction(event.getAction()),
                ItemSlot.OFFHAND
        );
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            return;
        }
        if (event.getEntity() instanceof Player player) {
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getItemInMainHand(),
                    ItemBehaviour.DAMAGE_RECEIVED,
                    ItemSlot.MAINHAND
            );
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getItemInOffHand(),
                    ItemBehaviour.DAMAGE_RECEIVED,
                    ItemSlot.OFFHAND
            );
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getHelmet(),
                    ItemBehaviour.DAMAGE_RECEIVED,
                    ItemSlot.HEAD
            );
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getChestplate(),
                    ItemBehaviour.DAMAGE_RECEIVED,
                    ItemSlot.CHEST
            );
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getLeggings(),
                    ItemBehaviour.DAMAGE_RECEIVED,
                    ItemSlot.LEGS
            );
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getBoots(),
                    ItemBehaviour.DAMAGE_RECEIVED,
                    ItemSlot.FEET
            );
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player) {
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getItemInMainHand(),
                    ItemBehaviour.DAMAGE_DEALT,
                    ItemSlot.MAINHAND
            );
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getItemInOffHand(),
                    ItemBehaviour.DAMAGE_DEALT,
                    ItemSlot.OFFHAND
            );
        }
        if (event.getEntity() instanceof Player player) {
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getItemInMainHand(),
                    ItemBehaviour.DAMAGE_RECEIVED,
                    ItemSlot.MAINHAND
            );
            handleItem(
                    event,
                    player,
                    player.getLocation(),
                    player.getWorld().getBlockAt(player.getLocation()),
                    player.getInventory().getItemInOffHand(),
                    ItemBehaviour.DAMAGE_RECEIVED,
                    ItemSlot.OFFHAND
            );
        }
    }

    private void handleItem(Event event, Player player, Location location, Block block, ItemStack item, ItemBehaviour itemBehaviour, ItemSlot itemSlot) {
        if (item != null && item.getItemMeta() != null) {
            CustomItemStack customItemStack = new CustomItemStackBuilder(item).build();

            //check to see if the itemStack applies
            if (
                    customItemStack.getItemEffects().isEmpty() ||
                            customItemStack.getBehaviour() == null ||
                            !customItemStack.getBehaviour().equals(itemBehaviour) ||
                            !customItemStack.getBehaviourSlot().equals(itemSlot)
            ) {
                return;
            }

            //play effects (if any)
            Set<Effect> effects = customItemStack.getItemEffects();
            if (effects != null) {
                for (Effect effect : effects) {
                    try {
                        EffectTags annotation = effect.getClass().getAnnotation(EffectTags.class);
                        if (annotation != null) {
                            if (annotation.allowedItemBehaviours() != null && annotation.allowedItemBehaviours().length > 0 &&
                                !ArrayUtils.contains(annotation.allowedItemBehaviours(), itemBehaviour)) {
                                continue;
                            }
                            if (annotation.allowedItemSlots() != null && annotation.allowedItemSlots().length > 0 &&
                                    !ArrayUtils.contains(annotation.allowedItemSlots(), itemSlot)) {
                                continue;
                            }
                        }
                        handleEffect(player, location, block, effect, event);
                    } catch (Exception e) {
                        Bukkit.getLogger().log(Level.WARNING, "Failed in playing effect: " + effect.getName());
                        Bukkit.getLogger().log(Level.WARNING, e.getMessage());
                    }
                }
            }
            itemSlot.updateItem(player, customItemStack);
        }
    }

    private void handleEffect(Player player, Location location, Block block, Effect effect, Event event) {
        if (effect instanceof PlayerEffect playerEffect) {
            playerEffect.playEffect(player, event);
        } else if (effect instanceof LocationEffect locationEffect) {
            locationEffect.playEffect(location, event);
        } else if (effect instanceof BlockEffect blockEffect) {
            blockEffect.playEffect(block, event);
        } else {
            Bukkit.getLogger().log(Level.WARNING, "Unrecognised effect type for: " + effect.getClass().getName());
        }
    }
}