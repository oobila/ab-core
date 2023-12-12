package com.github.oobila.bukkit.sidecar.resource;

import lombok.NoArgsConstructor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.io.IOException;
import java.io.InputStream;

@NoArgsConstructor
public abstract class YAMLResource extends Resource implements ConfigurationSerializable {

    @Override
    public void loadMetaData(InputStream inputStream) {
        //data loaded within construction
    }

    static <T extends YAMLResource> T construct(InputStream inputStream, Class<T> type) throws IOException {
        return null;// SidecarConfiguration.load(inputStream, type, null);
    }

    @Override
    public void loadData(InputStream inputStream) {
        //no data to load
    }

    @Override
    public void unloadData() {
        //no data to unload
    }
}
