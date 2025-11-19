package com.countryclub.model;

public class Venue {
    private int venueId;
    private String venueName;
    private String venueType;
    private int capacity;
    private String availabilityStatus;

    public Venue() {
    }

    public Venue(String venueName) {
        this.venueName = venueName;
    }

    public Venue(int venueId, String venueName, String venueType, int capacity, String availabilityStatus) {
        this.venueId = venueId;
        this.venueName = venueName;
        this.venueType = venueType;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }

    
    public int getVenueId() { return venueId; }
    public void setVenueId(int venueId) { this.venueId = venueId; }

    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }

    public String getVenueType() { return venueType; }
    public void setVenueType(String venueType) { this.venueType = venueType; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getAvailabilityStatus() { return availabilityStatus; }
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }

    @Override
    public String toString() {
        return venueName;
    }
}