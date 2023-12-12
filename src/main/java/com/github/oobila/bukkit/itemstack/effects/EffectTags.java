package com.github.oobila.bukkit.itemstack.effects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EffectTags {

    ItemSlot[] allowedItemSlots();
    ItemBehaviour[] allowedItemBehaviours();

}
