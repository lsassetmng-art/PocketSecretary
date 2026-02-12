package com.lsam.pocketsecretary.persona;

public class PersonaModel {
    public String name;
    public String prefix;
    public String suffix;
    public String emotion;

    public PersonaModel(String name, String prefix, String suffix, String emotion) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.emotion = emotion;
    }
}