package com.countryclub.model;

/**
 * A simple model class (DTO) to hold data for one row of the Event Report.
 */
public class EventReportItem {
    private int eventId;
    private String eventName;
    private double totalCost;

    public EventReportItem(int eventId, String eventName, double totalCost) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.totalCost = totalCost;
    }

    // Getters
    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public double getTotalCost() { return totalCost; }
}