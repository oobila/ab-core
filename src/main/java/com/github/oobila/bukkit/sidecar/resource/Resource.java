package com.github.oobila.bukkit.sidecar.resource;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@NoArgsConstructor
public abstract class Resource<T> {

    @Getter
    String folder;

    @Getter
    String name;

    @Getter
    String fileExtension;

    @Getter
    Class<? extends Resource> type;

    @Getter
    protected T data;

    public abstract void loadMetaData(InputStream inputStream);

    public abstract void loadData(InputStream inputStream);

    public abstract void unloadData();

}
