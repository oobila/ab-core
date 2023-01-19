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
import java.util.List;
import java.util.Map;

public class PluginPersistentDataTest {

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
     * This test checks that a string can be saved and loaded with the PluginPersistentData annotation
     */

    @PluginPersistentData(path = "test_string.yml")
    public static String string = "this is the test string";

    @PluginPersistentData(path = "test_string.yml")
    public static String stringReturn;

    @Test
    public void saveAndLoadAString() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveData(plugin, PluginPersistentDataTest.class.getDeclaredField("string"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_string.yml").exists());
        DataLoader.loadData(plugin, PluginPersistentDataTest.class.getDeclaredField("stringReturn"));
        Assert.assertEquals(stringReturn, string);
    }

    /**
     * This test checks that an integer can be saved and loaded with the PluginPersistentData annotation
     */

    @PluginPersistentData(path = "test_int.yml")
    public static int integer = 18;

    @PluginPersistentData(path = "test_int.yml")
    public static int integerReturn;

    @Test
    public void saveAndLoadAnInt() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveData(plugin, PluginPersistentDataTest.class.getDeclaredField("integer"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_int.yml").exists());
        DataLoader.loadData(plugin, PluginPersistentDataTest.class.getDeclaredField("integerReturn"));
        Assert.assertEquals(integer, integerReturn);
    }

    /**
     * This test checks that an object can be saved and loaded with the PluginPersistentData annotation
     */

    @PluginPersistentData(path = "test_object.yml")
    public static TestObject object = new TestObject();

    @PluginPersistentData(path = "test_object.yml")
    public static TestObject objectReturn;

    @Test
    public void saveAndLoadAnObject() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveData(plugin, PluginPersistentDataTest.class.getDeclaredField("object"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_object.yml").exists());
        DataLoader.loadData(plugin, PluginPersistentDataTest.class.getDeclaredField("objectReturn"));
        Assert.assertEquals(object, objectReturn);
    }

    /**
     * This test checks that a list of strings can be saved and loaded with the PluginPersistentData annotation
     */

    @PluginPersistentData(path = "test_list_string.yml")
    public static List<String> list = List.of("a", "b", "c");

    @PluginPersistentData(path = "test_list_string.yml")
    public static List<String> listReturn;

    @Test
    public void saveAndLoadAStringList() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveData(plugin, PluginPersistentDataTest.class.getDeclaredField("list"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_list_string.yml").exists());
        DataLoader.loadData(plugin, PluginPersistentDataTest.class.getDeclaredField("listReturn"));
        Assert.assertEquals(list, listReturn);
    }

    /**
     * This test checks that a list of objects can be saved and loaded with the PluginPersistentData annotation
     */

    @PluginPersistentData(path = "test_list_object.yml")
    public static List<TestObject> list2 = List.of(new TestObject("object a"), new TestObject("object b"));

    @PluginPersistentData(path = "test_list_object.yml")
    public static List<TestObject> list2Return;

    @Test
    public void saveAndLoadAnObjectList() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveData(plugin, PluginPersistentDataTest.class.getDeclaredField("list2"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_list_object.yml").exists());
        DataLoader.loadData(plugin, PluginPersistentDataTest.class.getDeclaredField("list2Return"));
        Assert.assertEquals(list2, list2Return);
    }

    /**
     * This test checks that a map of strings can be saved and loaded with the PluginPersistentData annotation
     */

    @PluginPersistentData(path = "test_map_string.yml")
    public static Map<String, String> map = Map.of("a", "0", "b", "1", "c", "2");

    @PluginPersistentData(path = "test_map_string.yml")
    public static Map<String, String> mapReturn;

    @Test
    public void saveAndLoadAStringMap() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveData(plugin, PluginPersistentDataTest.class.getDeclaredField("map"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_map_string.yml").exists());
        DataLoader.loadData(plugin, PluginPersistentDataTest.class.getDeclaredField("mapReturn"));
        Assert.assertEquals(map, mapReturn);
    }

    /**
     * This test checks that a map of objects can be saved and loaded with the PluginPersistentData annotation
     */

    @PluginPersistentData(path = "test_map_object.yml")
    public static Map<String, TestObject> map2 = Map.of("a", new TestObject("object a"),
            "b", new TestObject("object b"));

    @PluginPersistentData(path = "test_map_object.yml")
    public static Map<String, String> map2Return;

    @Test
    public void saveAndLoadAnObjectMap() throws NoSuchFieldException, IOException, ClassNotFoundException, IllegalAccessException {
        DataLoader.saveData(plugin, PluginPersistentDataTest.class.getDeclaredField("map2"));
        Assert.assertTrue(new File(plugin.getDataFolder(), "test_map_object.yml").exists());
        DataLoader.loadData(plugin, PluginPersistentDataTest.class.getDeclaredField("map2Return"));
        Assert.assertEquals(map2, map2Return);
    }

}