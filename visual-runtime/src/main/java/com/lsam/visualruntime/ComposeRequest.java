package com.lsam.visualruntime;

public class ComposeRequest {
    public final String personaId;
    public final String layersJson;
    public final int width;
    public final int height;
    public final String outputName;

    public ComposeRequest(
            String personaId,
            String layersJson,
            int width,
            int height,
            String outputName
    ) {
        this.personaId = personaId;
        this.layersJson = layersJson;
        this.width = width;
        this.height = height;
        this.outputName = outputName;
    }
}
