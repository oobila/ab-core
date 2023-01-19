package com.github.oobila.bukkit.entity;

import lombok.Getter;
import org.bukkit.entity.Entity;

@Getter
public abstract class CustomEntityBehaviour<T extends NodeEntity<? extends Entity>> {

    private T entity;
    private int t;

    protected CustomEntityBehaviour(T entity, int t) {
        this.entity = entity;
        this.t = t;
    }

    public abstract void onNextTick();

}
