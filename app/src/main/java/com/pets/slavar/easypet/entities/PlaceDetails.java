package com.pets.slavar.easypet.entities;

/**
 * Created by Slavar on 7/3/2016.
 */
public class PlaceDetails {

    private String phoneNumber;
    private String rating;
    private String webSite;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public PlaceDetails(String phoneNumber, String rating, String webSite, String address) {
        this.phoneNumber = phoneNumber;
        this.rating = rating;
        this.webSite = webSite;
        this.address = address;
    }

    public String getPhoneNumber() {

        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }
}
