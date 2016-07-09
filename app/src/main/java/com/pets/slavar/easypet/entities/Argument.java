package com.pets.slavar.easypet.entities;

import android.app.ProgressDialog;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Slavar on 6/15/2016.
 */
public class Argument implements Parcelable{
    private Category category;
    private Coordinates coordinates;
    private String name;

    protected Argument(Parcel in) {
        category = in.readParcelable(Category.class.getClassLoader());
        coordinates = in.readParcelable(Coordinates.class.getClassLoader());
        name = in.readString();
    }

    public static final Creator<Argument> CREATOR = new Creator<Argument>() {
        @Override
        public Argument createFromParcel(Parcel in) {
            return new Argument(in);
        }

        @Override
        public Argument[] newArray(int size) {
            return new Argument[size];
        }
    };

    public Argument() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(category, flags);
        dest.writeParcelable(coordinates, flags);
        dest.writeString(name);
    }

}
