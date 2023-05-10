package com.github.oobila.bukkit.sidecar.resource;

import com.github.oobila.bukkit.worldedit.SchematicLoadOperationBuilder;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;

public abstract class SchematicResource extends Resource {

    @Getter
    protected Clipboard clipboard;

    @Override
    public void loadData(InputStream inputStream) {
        try {
            clipboard = new SchematicLoadOperationBuilder(inputStream).clipboard();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unloadData() {
        clipboard = null;
    }
}
