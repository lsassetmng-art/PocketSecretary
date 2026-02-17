package com.lsam.pocketsecretary.ui.event;

public class CalendarCellModel {

    public int year;
    public int month0;
    public int day;

    public boolean isToday;
    public boolean hasEvent;

    public CalendarCellModel(
            int year,
            int month0,
            int day,
            boolean isToday,
            boolean hasEvent
    ) {
        this.year = year;
        this.month0 = month0;
        this.day = day;
        this.isToday = isToday;
        this.hasEvent = hasEvent;
    }
}