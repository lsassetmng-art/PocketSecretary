package com.lsam.pocketsecretary.core.personaos.provider;

import com.lsam.pocketsecretary.core.personaos.model.PersonaRequest;
import com.lsam.pocketsecretary.core.personaos.model.PersonaResponse;

public interface PersonaProvider {
    PersonaResponse generate(PersonaRequest req);
    String providerName();
}