package com.github.oobila.bukkit.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RandomUtilTest {

    @Test
    public void testRandomProducesAllPossibleResults() {
        Set<Integer> integers = Set.of(1, 2);
        Map<Integer, Integer> resultCount = new HashMap<>();
        integers.forEach(integer -> resultCount.put(integer, 0));
        resultCount.put(null, 0);
        for(int i = 0; i < 10; i++) {
            Integer value = RandomUtil.getRandomFromSet(integers);
            resultCount.put(value, resultCount.get(value) + 1);
        }
        Assert.assertTrue(true);
    }

}