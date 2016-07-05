package com.pets.slavar.easypet.entities;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Double2;

/**
 * Created by Slavar on 6/16/2016.
 */
public class Result implements Parcelable{
    private Coordinates coordinates;
    private String imageUrl;
    private String id;
    private String name;
    private String place_id;
    private String address;
    private float distance;

    public Result(Coordinates coordinates, String imageUrl, String id, String name, String place_id, String address, Coordinates currentCoordinates) {
        this.coordinates = coordinates;
        this.imageUrl = imageUrl;
        this.id = id;
        this.name = name;
        this.place_id = place_id;
        this.address = address;
        this.distance = calculateDistance(currentCoordinates);

    }

    protected Result(Parcel in) {
        coordinates = in.readParcelable(Coordinates.class.getClassLoader());
        imageUrl = in.readString();
        id = in.readString();
        name = in.readString();
        place_id = in.readString();
        address = in.readString();
        distance = in.readFloat();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float calculateDistance(Coordinates myLocation) {
        Location destination = new Location("");
        destination.setLatitude(Double.valueOf(getCoordinates().getLatitude()));
        destination.setLongitude(Double.valueOf(getCoordinates().getLongitude()));
        Location currentLocation = new Location("");
        currentLocation.setLatitude(Double.parseDouble(myLocation.getLatitude()));
        currentLocation.setLongitude(Double.parseDouble(myLocation.getLongitude()));
        float result = currentLocation.distanceTo(destination) / 1000;
        return result;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(coordinates, flags);
        dest.writeString(imageUrl);
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(place_id);
        dest.writeString(address);
        dest.writeFloat(distance);
    }
}
