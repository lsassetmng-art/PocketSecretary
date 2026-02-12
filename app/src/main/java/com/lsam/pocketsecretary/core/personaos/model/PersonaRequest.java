package com.lsam.pocketsecretary.core.personaos.model;

public final class PersonaRequest {
    public final PersonaContext context;
    public final Constraints constraints;

    public PersonaRequest(PersonaContext context, Constraints constraints) {
        this.context = context;
        this.constraints = constraints;
    }

    public static final class Constraints {
        public final int maxChars;
        public final String toneSafety;
        public final boolean noSensitiveData;

        public Constraints(int maxChars, String toneSafety, boolean noSensitiveData) {
            this.maxChars = maxChars;
            this.toneSafety = toneSafety;
            this.noSensitiveData = noSensitiveData;
        }

        public static Constraints defaults() {
            return new Constraints(120, "SAFE_ONLY", true);
        }
    }
}