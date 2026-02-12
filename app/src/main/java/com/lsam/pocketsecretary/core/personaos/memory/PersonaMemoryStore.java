package com.lsam.pocketsecretary.core.personaos.memory;

public interface PersonaMemoryStore {
    void bumpOpenedNotification(String personaId);
    void bumpManualTrigger(String personaId);
    void bumpSpeechTool(String personaId);
}