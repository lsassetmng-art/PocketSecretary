package com.lsam.pocketsecretary.core.personaos.visual;

public final class VisualComposeResponse {

    public final boolean success;
    public final String jobId;
    public final String outputPath;
    public final String error;

    private VisualComposeResponse(boolean success, String jobId, String outputPath, String error) {
        this.success = success;
        this.jobId = jobId;
        this.outputPath = outputPath;
        this.error = error;
    }

    public static VisualComposeResponse success(String jobId, String outputPath) {
        return new VisualComposeResponse(true, jobId, outputPath, null);
    }

    public static VisualComposeResponse failure(String error) {
        return new VisualComposeResponse(false, null, null, error);
    }
}
