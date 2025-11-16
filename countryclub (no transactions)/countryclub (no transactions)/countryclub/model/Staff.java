package com.countryclub.model;

import java.time.LocalDate;

public class Staff {
    private int staffId;
    private String contactNo;
    private String email;
    private String staffName;
    private LocalDate birthDate;
    private String roleName;
    private String availabilityStatus;

    // Constructor
    public Staff(int staffId, String contactNo, String email, String staffName, LocalDate birthDate, String roleName,
            String availabilityStatus) {
        this.staffId = staffId;
        this.contactNo = contactNo;
        this.email = email;
        this.staffName = staffName;
        this.birthDate = birthDate;
        this.roleName = roleName;
        this.availabilityStatus = availabilityStatus;
    }

    // Getters
    public int getStaffId() { return staffId; }
    public String getContactNo() { return contactNo; }
    public String getEmail() { return email; }
    public String getStaffName() { return staffName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getRoleName() { return roleName; }
    public String getAvailabilityStatus() { return availabilityStatus; }
}