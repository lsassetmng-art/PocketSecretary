package com.lsam.pocketsecretary.core.personaos;

public class ReasonToneMapper {

    public String mapReasonToTone(String reasonCode, String baseTone) {

        if (reasonCode == null) return baseTone;

        if (reasonCode.contains("DELAY")) {
            return "concerned_soft";
        }

        if (reasonCode.contains("SUCCESS")) {
            return "warm_positive";
        }

        if (reasonCode.contains("ALERT")) {
            return "firm_gentle";
        }

        return baseTone;
    }
}
