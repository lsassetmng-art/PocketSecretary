package com.lsam.pocketsecretary.core.event;

public class Event {

    public final String title;
    public final long time;
    public final String note;

    public Event(String title, long time, String note){
        this.title = title;
        this.time = time;
        this.note = note;
    }
}
