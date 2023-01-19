package com.github.oobila.bukkit.util;

import org.apache.commons.lang.StringUtils;

public class Version implements Comparable<Version> {

    private String versionString;

    public final String get() {
        return this.versionString;
    }

    public Version(String version) {
        if(version == null)
            throw new IllegalArgumentException("Version can not be null");
        if(!version.matches("[0-9]+(?:\\.[0-9]+)*+(?:-\\w*|)"))
            throw new IllegalArgumentException("Invalid version format");
        this.versionString = version;
    }

    @Override public int compareTo(Version that) {
        if(that == null) {
            return 1;
        }
        String[] thisParts = this.get().split("[\\.-]");
        String[] thatParts = that.get().split("[\\.-]");
        int length = Math.max(thisParts.length, thatParts.length);
        for(int i = 0; i < length; i++) {
            if (thisParts.length < i + 1) {
                return -1;
            } else if (thatParts.length < i + 1) {
                return 1;
            } else if (StringUtils.isNumeric(thisParts[i]) && StringUtils.isNumeric(thatParts[i])) {
                int thisInt = Integer.parseInt(thisParts[i]);
                int thatInt = Integer.parseInt(thatParts[i]);
                if(thisInt < thatInt) {
                    return -1;
                } else if(thisInt > thatInt) {
                    return 1;
                }
            }
        }
        return 0;
    }

    @Override public boolean equals(Object that) {
        if(this == that)
            return true;
        if(that == null)
            return false;
        if(this.getClass() != that.getClass())
            return false;
        return this.versionString.compareTo(((Version) that).versionString) == 0;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}