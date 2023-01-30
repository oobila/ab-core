package com.github.oobila.bukkit.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("java:S5786")
public class VersionTest {

    @ParameterizedTest
    @CsvSource({
            "1.2.3.4", "1.2.3.4",
            "1.2.3.4-SNAPSHOT", "1.2.3.4-SNAPSHOT"
    })
    public void testVersionsAreEqual(String v1, String v2) {
        Version version1 = new Version(v1);
        Version version2 = new Version(v2);
        Assert.assertEquals(version1, version2);
    }

    @Test
    public void testDifferentComplexVersionsAreNotEqual() {
        Version version1 = new Version("1.2.3.4-SNAPSHOT");
        Version version2 = new Version("1.2.3.4-BUKKIT");
        Assert.assertNotEquals(version1, version2);
    }

    @Test
    public void testMixedVersionsAreNotEqual() {
        Version version1 = new Version("1.2.3.4-SNAPSHOT");
        Version version2 = new Version("1.2.3.4");
        Assert.assertNotEquals(version1, version2);
    }

    @ParameterizedTest
    @CsvSource({
            "1.2.3.4", "1.2.3.4",
            "1.2.3.4-SNAPSHOT", "1.2.3.4-SNAPSHOT",
            "1.2.3.4-SNAPSHOT", "1.2.3.4-BUKKIT"
    })
    public void testVersionsCompareEqual(String v1, String v2) {
        Version version1 = new Version(v1);
        Version version2 = new Version(v2);
        Assert.assertEquals(0, version1.compareTo(version2));
    }

    @Test
    public void testMixedVersionsCompareDifferent() {
        Version version1 = new Version("1.2.3.4-SNAPSHOT");
        Version version2 = new Version("1.2.3.4");
        Assert.assertNotEquals(0, version1.compareTo(version2));
    }

    @Test
    public void testNumberVersionsCompareDifferent() {
        Version version1 = new Version("1.2.3.4");
        Version version2 = new Version("1.2.3.14");
        Assert.assertTrue(version1.compareTo(version2) < 0);
    }

    @Test
    public void testNumberVersionsCompareDifferent2() {
        Version version1 = new Version("1.12.3.4");
        Version version2 = new Version("1.2.3.4");
        Assert.assertTrue(version1.compareTo(version2) > 0);
    }

    @ParameterizedTest
    @CsvSource({
            "1.2", "1.2.3.4",
            "1.2.3.4", "2.2"
    })
    void testShortVersionsCompareDifferent(String v1, String v2) {
        Version version1 = new Version(v1);
        Version version2 = new Version(v2);
        Assert.assertTrue(version1.compareTo(version2) < 0);
    }

}
