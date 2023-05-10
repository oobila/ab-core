package com.github.oobila.bukkit.sidecar.resource;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public abstract class ResourcePack {

    @Getter
    Map<String, Map<String, Resource>> resourceMap = new HashMap<>();

    @Getter
    String name;

    File file;

    public void load() {
        ResourcePackLoader.loadResourcePack(this);
    }

    public void unload() {
        resourceMap.forEach(
                (strings, stringResourceMap) -> stringResourceMap.forEach(
                        (s, resource) -> resource.unloadData()
                )
        );
    }

}
