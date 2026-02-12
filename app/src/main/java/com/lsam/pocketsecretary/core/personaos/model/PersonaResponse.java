package com.lsam.pocketsecretary.core.personaos.model;

import java.util.List;

public final class PersonaResponse {
    public final String requestId;
    public final String personaId;
    public final String text;
    public final PersonaToneTag toneTag;
    public final List<String> reasonCodes;

    public final String version;               // persona_version
    public final boolean backwardCompatible;   // backward_compatible

    public PersonaResponse(
            String requestId,
            String personaId,
            String text,
            PersonaToneTag toneTag,
            List<String> reasonCodes,
            String version,
            boolean backwardCompatible
    ) {
        this.requestId = requestId;
        this.personaId = personaId;
        this.text = text;
        this.toneTag = toneTag;
        this.reasonCodes = reasonCodes;
        this.version = version;
        this.backwardCompatible = backwardCompatible;
    }
}