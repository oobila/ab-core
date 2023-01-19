package com.github.oobila.bukkit.sidecar.persistence;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import com.github.oobila.bukkit.CorePlugin;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class PluginPersistentDataClusterTest {

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
     * This test checks that an object can be saved and loaded with the PluginPersistentDataCluster annotation
     */

    @PluginPersistentDataCluster(path = "test_cluster")
    public static Map<String, TestObject> map = Map.of("a", new TestObject("object a"),
            "b", new TestObject("object b"));

    @PluginPersistentDataCluster(path = "test_cluster")
    public static Map<String, TestObject> mapReturn;

    @Test
    public void saveAndLoadAnObject() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveDataCluster(plugin, PluginPersistentDataClusterTest.class.getDeclaredField("map"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster").isDirectory());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster/a.yml").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster/b.yml").exists());
        DataLoader.loadDataCluster(plugin, PluginPersistentDataClusterTest.class.getDeclaredField("mapReturn"));
        Assert.assertEquals(map, mapReturn);
    }

    /**
     * This test checks that an object can be saved and loaded with the PluginPersistentDataCluster annotation and uuid KeySerializer
     */

    @PluginPersistentDataCluster(path = "test_cluster_uuid")
    public static Map<UUID, TestObject> uuidMap = Map.of(UUID.fromString("00000000-0000-0000-0000-000000000000"), new TestObject("object a"),
            UUID.fromString("11111111-0000-0000-0000-000000000000"), new TestObject("object b"));

    @PluginPersistentDataCluster(path = "test_cluster_uuid")
    public static Map<UUID, TestObject> uuidMapReturn;

    @Test
    public void saveAndLoadAnObjectWithUUIDKeySerializer() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveDataCluster(plugin, PluginPersistentDataClusterTest.class.getDeclaredField("uuidMap"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid").isDirectory());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid/00000000-0000-0000-0000-000000000000.yml").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_uuid/11111111-0000-0000-0000-000000000000.yml").exists());
        DataLoader.loadDataCluster(plugin, PluginPersistentDataClusterTest.class.getDeclaredField("uuidMapReturn"));
        Assert.assertEquals(uuidMap, uuidMapReturn);
    }

    /**
     * This test checks that an object can be saved and loaded with the PluginPersistentDataCluster annotation and OfflinePlayer KeySerializer
     */

    @PluginPersistentDataCluster(path = "test_cluster_player")
    public static Map<OfflinePlayer, TestObject> playerMap;

    @PluginPersistentDataCluster(path = "test_cluster_player")
    public static Map<OfflinePlayer, TestObject> playerMapReturn;

    @Test
    public void saveAndLoadAnObjectWithPlayerKeySerializer() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        OfflinePlayer player1 = server.addPlayer();
        OfflinePlayer player2 = server.addPlayer();
        playerMap = Map.of(player1, new TestObject("object a"), player2, new TestObject("object b"));

        DataLoader.saveDataCluster(plugin, PluginPersistentDataClusterTest.class.getDeclaredField("playerMap"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_player").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_player").isDirectory());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_player/" + player1.getUniqueId().toString() + ".yml").exists());
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_cluster_player/" + player1.getUniqueId().toString() + ".yml").exists());
        DataLoader.loadDataCluster(plugin, PluginPersistentDataClusterTest.class.getDeclaredField("playerMapReturn"));
        Assert.assertEquals(playerMap, playerMapReturn);
    }

}
