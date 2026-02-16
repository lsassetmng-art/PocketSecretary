package com.lsam.pocketsecretary.core.personaos.edge;

public final class PersonaStateApplyResponse {

    public final boolean success;
    public final int revision;
    public final String nextVisualJobId;
    public final String error;

    private PersonaStateApplyResponse(boolean success, int revision, String job, String error) {
        this.success = success;
        this.revision = revision;
        this.nextVisualJobId = job;
        this.error = error;
    }

    public static PersonaStateApplyResponse success(int revision, String job) {
        return new PersonaStateApplyResponse(true, revision, job, null);
    }

    public static PersonaStateApplyResponse failure(String error) {
        return new PersonaStateApplyResponse(false, 0, null, error);
    }
}
