package com.countryclub.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Event {
    private int eventId;
    private String eventName;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalTime fromTime;
    private LocalTime toTime;
    private int itemId;
    private int memberId;
    private int staffInchargeId;
    private int venueId;

    // Constructor
    public Event(int eventId, String eventName, LocalDate fromDate, LocalDate toDate, LocalTime fromTime,
            LocalTime toTime, int itemId, int memberId, int staffInchargeId, int venueId) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.itemId = itemId;
        this.memberId = memberId;
        this.staffInchargeId = staffInchargeId;
        this.venueId = venueId;
    }

    // Getters
    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getToDate() { return toDate; }
    public LocalTime getFromTime() { return fromTime; }
    public LocalTime getToTime() { return toTime; }
    public int getItemId() { return itemId; }
    public int getMemberId() { return memberId; }
    public int getStaffInchargeId() { return staffInchargeId; }
    public int getVenueId() { return venueId; }
}