package com.example.alfhana.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationHelper implements Parcelable {

    private double longtiude;
    private double latitude;

    public LocationHelper(double longtiude, double latitude) {
        this.longtiude = longtiude;
        this.latitude = latitude;
    }

    protected LocationHelper(Parcel in) {
        longtiude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<LocationHelper> CREATOR = new Creator<LocationHelper>() {
        @Override
        public LocationHelper createFromParcel(Parcel in) {
            return new LocationHelper(in);
        }

        @Override
        public LocationHelper[] newArray(int size) {
            return new LocationHelper[size];
        }
    };

    public double getLongtiude() {
        return longtiude;
    }

    public void setLongtiude(double longtiude) {
        this.longtiude = longtiude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(longtiude);
        parcel.writeDouble(latitude);
    }
}
