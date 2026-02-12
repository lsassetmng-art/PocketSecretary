package com.lsam.pocketsecretary.core.personaos.provider;

import com.lsam.pocketsecretary.core.personaos.model.PersonaRequest;
import com.lsam.pocketsecretary.core.personaos.model.PersonaResponse;

/**
 * Phase G:
 * Remote Persona Provider (Design Stub Only)
 * 
 * Not implemented intentionally.
 * This class defines the boundary for future PersonaOS integration.
 */
public final class RemotePersonaProvider implements PersonaProvider {

    @Override
    public PersonaResponse generate(PersonaRequest req) {
        throw new UnsupportedOperationException(
            "RemotePersonaProvider is not implemented (Phase G design only)"
        );
    }

    @Override
    public String providerName() {
        return "RemotePersonaProvider";
    }
}