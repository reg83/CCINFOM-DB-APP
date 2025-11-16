package com.countryclub.model;

import java.time.LocalDate;

public class Member {
    private int memberId;
    private int membershipTypeId;
    private String contactNo;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private LocalDate joinDate;
    private LocalDate expiryDate;
    private String email;

    // Constructor
    public Member(int memberId, int membershipTypeId, String contactNo, String firstName, String lastName,
            LocalDate birthDate, LocalDate joinDate, LocalDate expiryDate, String email) {
        this.memberId = memberId;
        this.membershipTypeId = membershipTypeId;
        this.contactNo = contactNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.joinDate = joinDate;
        this.expiryDate = expiryDate;
        this.email = email;
    }

    // Getters
    public int getMemberId() { return memberId; }
    public int getMembershipTypeId() { return membershipTypeId; }
    public String getContactNo() { return contactNo; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public LocalDate getJoinDate() { return joinDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getEmail() { return email; }
}