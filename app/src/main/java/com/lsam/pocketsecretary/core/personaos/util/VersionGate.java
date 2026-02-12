package com.lsam.pocketsecretary.core.personaos.util;

public final class VersionGate {
    private VersionGate(){}

    public static boolean isAcceptable(String personaVersion, boolean backwardCompatible) {
        SemVer v = SemVer.parseOrNull(personaVersion);
        if (v == null) return false;
        if (!backwardCompatible) return false;
        return v.major >= 0;
    }
}