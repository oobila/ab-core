package com.github.oobila.bukkit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Entity;

public abstract class CustomEntityBehaviour<T extends NodeEntity<? extends Entity>> {

    @Setter(AccessLevel.PACKAGE) @Getter(AccessLevel.PACKAGE)
    private T nodeEntity;

    @Getter(AccessLevel.PACKAGE)
    private int tickSpacing;

    protected CustomEntityBehaviour(int tickSpacing) {
        this.tickSpacing = tickSpacing;
    }

    void onNextTick() {
        onNextTick(nodeEntity);
    }

    public abstract void onNextTick(T nodeEntity);
}
