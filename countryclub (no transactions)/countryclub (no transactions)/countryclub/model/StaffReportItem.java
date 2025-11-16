package com.countryclub.model;

import java.sql.Date;
import java.sql.Time;

/**
 * A simple model class (DTO) to hold data for one row of the Staff Report.
 */
public class StaffReportItem {
    private int staffId;
    private String staffName;
    private String eventName;
    private Date fromDate;
    private Date toDate;
    private Time fromTime;
    private Time toTime;

    public StaffReportItem(int staffId, String staffName, String eventName, Date fromDate, Date toDate, Time fromTime, Time toTime) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.eventName = eventName;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    // Getters
    public int getStaffId() { return staffId; }
    public String getStaffName() { return staffName; }
    public String getEventName() { return eventName; }
    public Date getFromDate() { return fromDate; }
    public Date getToDate() { return toDate; }
    public Time getFromTime() { return fromTime; }
    public Time getToTime() { return toTime; }
    
    public String getEventNameString() {
        return (eventName == null) ? "N/A" : eventName;
    }
}