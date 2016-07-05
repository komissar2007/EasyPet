package com.pets.slavar.easypet.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Slavar on 6/14/2016.
 */
public class Coordinates implements Parcelable {
    String longitude;
    String latitude;

    public Coordinates(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected Coordinates(Parcel in) {
        longitude = in.readString();
        latitude = in.readString();
    }

    public static final Creator<Coordinates> CREATOR = new Creator<Coordinates>() {
        @Override
        public Coordinates createFromParcel(Parcel in) {
            return new Coordinates(in);
        }

        @Override
        public Coordinates[] newArray(int size) {
            return new Coordinates[size];
        }
    };

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String print() {
        return "Coordinates Are: Latitude - " + getLatitude() + ", Longitude - " + getLongitude();
    }

    public boolean isValid() {
        //TODO
        return true;
    }

    public String getLocation() {
        String location = getLatitude() + "," + getLongitude();
        return location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(longitude);
        dest.writeString(latitude);
    }
}
