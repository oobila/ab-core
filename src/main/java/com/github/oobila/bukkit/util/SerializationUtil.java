package com.github.oobila.bukkit.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SerializationUtil {

    public static void writeVector(ObjectOutputStream o, Vector vector) throws IOException {
        if(vector != null) {
            o.writeObject(true);
            o.writeObject(vector.getX());
            o.writeObject(vector.getY());
            o.writeObject(vector.getZ());
        } else {
            o.writeObject(false);
        }
    }

    public static Vector readVector(ObjectInputStream o) throws IOException, ClassNotFoundException {
        boolean hasAngle = (boolean) o.readObject();
        if(hasAngle) {
            double x = (double) o.readObject();
            double y = (double) o.readObject();
            double z = (double) o.readObject();
            return new Vector(x, y, z);
        }
        return null;
    }

    public static void writeEuler(ObjectOutputStream o, EulerAngle eulerAngle) throws IOException {
        if(eulerAngle != null) {
            o.writeObject(true);
            o.writeObject(eulerAngle.getX());
            o.writeObject(eulerAngle.getY());
            o.writeObject(eulerAngle.getZ());
        } else {
            o.writeObject(false);
        }
    }

    public static EulerAngle readEuler(ObjectInputStream o) throws IOException, ClassNotFoundException {
        boolean hasAngle = (boolean) o.readObject();
        if(hasAngle) {
            double x = (double) o.readObject();
            double y = (double) o.readObject();
            double z = (double) o.readObject();
            return new EulerAngle(x, y, z);
        }
        return null;
    }

}
