package com.aleenafatimakhalid.k201688;

import java.util.List;
public class ItemModel {
    private String name;
    private String hourlyRate;
    private String description;
    private String match;
    private List<String> imageUrls;
    private String userId;

    // A public, no-argument constructor
    public ItemModel() {}

    public ItemModel(String name, String hourlyRate, String description, String match, List<String> imageUrls, String userId) {
        this.name = name;
        this.hourlyRate = hourlyRate;
        this.description = description;
        this.match = match;
        this.imageUrls = imageUrls;
        this.userId = userId;
    }

    // Getters and setters for 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getters and setters for 'hourlyRate'
    public String getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(String hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    // Getters and setters for 'description'
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getters and setters for 'match'
    public String getMatch() {
        return match;
    }

    public void setMatch(String match) {
        this.match = match;
    }

    public List<String> getImageUrls() {return imageUrls;}

    public void setImageUrls(List<String> ImageUrls) {this.imageUrls = imageUrls;}
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
