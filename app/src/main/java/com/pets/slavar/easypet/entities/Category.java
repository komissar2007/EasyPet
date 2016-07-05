package com.pets.slavar.easypet.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by SLAVAR on 6/9/2016.
 */
public class Category implements Parcelable{
    private String imageUrl;
    private String name;

    protected Category(Parcel in) {
        imageUrl = in.readString();
        name = in.readString();
    }

    public Category(){

    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(name);
    }
}
