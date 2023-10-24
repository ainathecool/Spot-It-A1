package com.aleenafatimakhalid.k201688;

public class UserProfile {
    private String name;
    private String email;
    private String contactNumber;

    public UserProfile() {
        // Default constructor required for Firebase
    }

    public UserProfile(String name, String email, String contactNumber) {
        this.name = name;
        this.email = email;
        this.contactNumber = contactNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
