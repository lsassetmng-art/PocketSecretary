package com.lsam.pocketsecretary.core.personaos.visual;

public final class StoragePublicUrl {

    private StoragePublicUrl(){}

    public static String build(String projectRef, String bucket, String outputPath) {
        if (outputPath.startsWith("/")) {
            outputPath = outputPath.substring(1);
        }
        return "https://" + projectRef +
               ".supabase.co/storage/v1/object/public/" +
               bucket + "/" + outputPath;
    }
}
