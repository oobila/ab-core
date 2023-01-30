package com.github.oobila.bukkit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.bukkit.entity.Entity;

class CustomEntity <T extends Entity> implements Entity {

    @Delegate(types = Entity.class) @Getter(AccessLevel.PACKAGE)
    private T entity;

    public CustomEntity(T entity) {
        this.entity = entity;
    }

}
