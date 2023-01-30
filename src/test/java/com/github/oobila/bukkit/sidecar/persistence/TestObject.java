package com.github.oobila.bukkit.sidecar.persistence;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Objects;

public class TestObject implements ConfigurationSerializable {

    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a z");

    private String string;
    private int integer = 18;
    private long aLong = 1234567890;
    private boolean bool = true;
    private UUID uuid = UUID.fromString("0000-00-00-00-000000");
    private ZonedDateTime zonedDateTime = ZonedDateTime.parse("2022-07-21 23:20:00 pm +00:00", FORMATTER);
    private List<String> list = List.of("a", "b", "c");
    private Map<String, Integer> map = Map.of("a", 0, "b", 1, "c", 2);

    public TestObject() {
        this("test string");
    }

    public TestObject(String string) {
        this.string = string;
        zonedDateTime = ZonedDateTime.ofInstant(
                Instant.ofEpochSecond(zonedDateTime.toInstant().toEpochMilli()),
                ZonedDateTime.now().getZone());
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("String", string);
        map.put("int", integer);
        map.put("long", aLong);
        map.put("boolean", bool);
        map.put("UUID", uuid.toString());
        map.put("ZonedDateTime", zonedDateTime.toInstant().toEpochMilli());
        map.put("List", list);
        map.put("Map", this.map);
        return map;
    }

    public static TestObject deserialize(Map<String, Object> args) {
        TestObject testObject = new TestObject();
        testObject.string = (String) args.get("String");
        testObject.integer = (int) args.get("int");
        testObject.aLong = (int) args.get("long");
        testObject.bool = (boolean) args.get("boolean");
        testObject.uuid = UUID.fromString((String) args.get("UUID"));
        testObject.zonedDateTime = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli((long) args.get("ZonedDateTime")),
                ZonedDateTime.now().getZone()
        );
        testObject.list = (List<String>) args.get("List");
        testObject.map = (Map<String, Integer>) args.get("Map");
        return testObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject that = (TestObject) o;
        return
                integer == that.integer &&
                        aLong == that.aLong &&
                        bool == that.bool &&
                        Objects.equals(string, that.string) &&
                        Objects.equals(uuid, that.uuid) &&
                        Objects.equals(zonedDateTime, that.zonedDateTime) &&
                        Objects.equals(list, that.list) &&
                        Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(string, integer, aLong, bool, uuid, zonedDateTime, list, map);
    }
}
