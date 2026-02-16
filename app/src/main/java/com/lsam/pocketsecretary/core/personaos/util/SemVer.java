package com.lsam.pocketsecretary.core.personaos.util;

public final class SemVer {

    public final int major;
    public final int minor;
    public final int patch;

    public SemVer(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public static SemVer parseOrNull(String v) {

        if (v == null) return null;

        String[] p = v.trim().split("\\.");

        if (p.length != 3) return null;

        try {
            return new SemVer(
                    Integer.parseInt(p[0]),
                    Integer.parseInt(p[1]),
                    Integer.parseInt(p[2])
            );
        } catch (Exception e) {
            return null;
        }
    }
}
