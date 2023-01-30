package com.github.oobila.bukkit.util;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class to help with common random functions
 */
public class RandomUtil {

    private RandomUtil() {
    }

    /**
     * Returns a random item from a Set (or any Collection)
     * @param set
     * @param <T>
     * @return
     */
    public static <T> T getRandomFromSet(Collection<T> set) {
        return set.stream().skip(ThreadLocalRandom.current().nextInt(set.size())).findFirst().orElse(null);
    }

    /**
     * Returns a random double that is both bound and skewed
     * @param min
     * @param max
     * @param skew - value between 0 and 1. 0 will skew output closer to the min, 1 will skew closer to the max.
     * @return
     */
    public static double skewedBoundedDouble(double min, double max, double skew) {
        return Math.max(min, Math.min(max, skew + ThreadLocalRandom.current().nextGaussian() * 0.3));
    }

}
