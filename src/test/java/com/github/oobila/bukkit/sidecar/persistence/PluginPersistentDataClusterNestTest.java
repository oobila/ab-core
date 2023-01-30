package com.github.oobila.bukkit.sidecar.persistence;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.github.oobila.bukkit.CorePlugin;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class PluginPersistentDataClusterNestTest {

    private ServerMock server;
    private CorePlugin plugin;

    @Before
    public void setUp() {
        server = MockBukkit.mock();
        plugin = MockBukkit.load(CorePlugin.class);
        ConfigurationSerialization.registerClass(TestObject.class);
    }

    @After
    public void tearDown() {
        MockBukkit.unmock();
    }

    /**
     * This test checks that an object can be saved and loaded with the PluginPersistentDataClusterNest annotation
     */

    @PluginPersistentDataClusterNest(path = "test_cluster", fileName = "data.yml")
    public static Map<String, TestObject> map = Map.of("a", new TestObject("object a"),
            "b", new TestObject("object b"));

    @PluginPersistentDataClusterNest(path = "test_cluster", fileName = "data.yml")
    public static Map<String, TestObject> mapReturn;

    @Test
    public void saveAndLoadAnObject() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveDataClusterNest(plugin, PluginPersistentDataClusterNestTest.class.getDeclaredField("map"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster").isDirectory());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster/a").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster/a").isDirectory());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster/b").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster/b").isDirectory());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster/a/data.yml").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster/b/data.yml").exists());
        DataLoader.loadDataClusterNest(plugin, PluginPersistentDataClusterNestTest.class.getDeclaredField("mapReturn"));
        Assert.assertEquals(map, mapReturn);
    }

    /**
     * This test checks that an object can be saved and loaded with the PluginPersistentDataClusterNest annotation and uuid KeySerializer
     */

    @PluginPersistentDataClusterNest(path = "test_cluster_uuid", fileName = "data.yml")
    public static Map<UUID, TestObject> uuidMap = Map.of(UUID.fromString("00000000-0000-0000-0000-000000000000"), new TestObject("object a"),
            UUID.fromString("11111111-0000-0000-0000-000000000000"), new TestObject("object b"));

    @PluginPersistentDataClusterNest(path = "test_cluster_uuid", fileName = "data.yml")
    public static Map<UUID, TestObject> uuidMapReturn;

    @Test
    public void saveAndLoadAnObjectWithUUIDKeySerializer() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveDataClusterNest(plugin, PluginPersistentDataClusterNestTest.class.getDeclaredField("uuidMap"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid").isDirectory());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid/00000000-0000-0000-0000-000000000000").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid/00000000-0000-0000-0000-000000000000").isDirectory());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid/11111111-0000-0000-0000-000000000000").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid/11111111-0000-0000-0000-000000000000").isDirectory());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid/00000000-0000-0000-0000-000000000000/data.yml").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid/11111111-0000-0000-0000-000000000000/data.yml").exists());
        DataLoader.loadDataClusterNest(plugin, PluginPersistentDataClusterNestTest.class.getDeclaredField("uuidMapReturn"));
        Assert.assertEquals(uuidMap, uuidMapReturn);
    }

}
