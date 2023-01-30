package com.github.oobila.bukkit.util;

import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

/**
 * Utility class for common vector calculations
 */
public class VectorMaths {

    public static final Vector baseVector = new Vector(1, 0, 0);
    public static final Vector zeroVector = new Vector(0, 0, 0);

    private VectorMaths(){
    }

    /**
     * Calculates the angle between two vectors
     * @param a
     * @param b
     * @return
     */
    public static double angleBetween(Vector a, Vector b) {
        double dot = a.dot(b);
        double det = ((a.getX()*b.getZ()) - (a.getZ()*b.getX()));
        return Math.atan2(det, dot);
    }

    /**
     * Applies a rotation to a vector and returns the new rotated vector
     * @param v
     * @param angle
     * @return
     */
    public static Vector rotate2d(Vector v, double angle) {
        double newX = (Math.cos(angle) * v.getX()) - (Math.sin(angle) * v.getZ());
        double newZ = (Math.sin(angle) * v.getX()) + (Math.cos(angle) * v.getZ());
        return new Vector(newX, v.getY(), newZ);
    }

    /**
     * Creates a set of evenly spread position vectors in a circle around a point vector.
     * @param nPoints
     * @param radius
     * @param center
     * @return
     */
    public static Set<Vector> drawPointsAroundCircle(int nPoints, double radius, Vector center){
        Set<Vector> vectors = new HashSet<>();
        double slice = 2 * Math.PI / nPoints;
        for (int i = 0; i < nPoints; i++) {
            double angle = slice * i;
            double newX = center.getX() + radius * Math.cos(angle);
            double newZ = center.getZ() + radius * Math.sin(angle);
            Vector v = new Vector(newX, center.getY(), newZ);
            vectors.add(v);
        }
        return vectors;
    }

}