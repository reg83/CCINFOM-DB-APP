package com.countryclub.model;

public class EventReportItem {
    private int eventId;
    private String eventName;
    private double totalCost;  
    private double totalIncome; 

    public EventReportItem(int eventId, String eventName, double totalCost, double totalIncome) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.totalCost = totalCost;
        this.totalIncome = totalIncome;
    }

    public int getEventId() { return eventId; }
    public String getEventName() { return eventName; }
    public double getTotalCost() { return totalCost; }
    public double getTotalIncome() { return totalIncome; }
}