package com.aleenafatimakhalid.k201688;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ItemModel implements Parcelable {
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

    protected ItemModel(Parcel in) {
        name = in.readString();
        hourlyRate = in.readString();
        description = in.readString();
        match = in.readString();
        imageUrls = in.createStringArrayList();
        userId = in.readString();
    }

    public static final Creator<ItemModel> CREATOR = new Creator<ItemModel>() {
        @Override
        public ItemModel createFromParcel(Parcel in) {
            return new ItemModel(in);
        }

        @Override
        public ItemModel[] newArray(int size) {
            return new ItemModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(hourlyRate);
        parcel.writeString(description);
        parcel.writeString(match);
        parcel.writeStringList(imageUrls);
        parcel.writeString(userId);
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

    public void setImageUrls(List<String> imageUrls) {this.imageUrls = imageUrls;}
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
