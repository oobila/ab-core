package com.github.oobila.bukkit.itemstack.effects;

import org.bukkit.event.block.Action;

public enum ItemBehaviour {

    RIGHT_CLICK_AIR,
    LEFT_CLICK_AIR,
    RIGHT_CLICK_BLOCK,
    LEFT_CLICK_BLOCK,
    PHYSICAL,
    DAMAGE_RECEIVED,
    DAMAGE_DEALT;

    public static ItemBehaviour fromAction(Action action) {
        switch (action) {
            case RIGHT_CLICK_AIR: return RIGHT_CLICK_AIR;
            case LEFT_CLICK_AIR: return LEFT_CLICK_AIR;
            case RIGHT_CLICK_BLOCK: return RIGHT_CLICK_BLOCK;
            case LEFT_CLICK_BLOCK: return LEFT_CLICK_BLOCK;
            case PHYSICAL:
            default: return PHYSICAL;
        }
    }

}
