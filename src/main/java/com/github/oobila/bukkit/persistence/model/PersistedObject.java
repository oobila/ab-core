package com.github.oobila.bukkit.persistence.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.time.ZonedDateTime;

public abstract class PersistedObject implements ConfigurationSerializable {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    private ZonedDateTime createdDate;

    public PersistedObject(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
