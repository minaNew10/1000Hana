package com.example.alfhana.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class Request implements Parcelable {
    private String phone;
    private String Uid;
    private String address;
    private String total;
    private List<Order> foods;


    public Request() {
    }

    public Request(String phone, String name, String address, String total, List<Order> foods) {
        this.phone = phone;
        this.Uid = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
    }

    protected Request(Parcel in) {
        phone = in.readString();
        Uid = in.readString();
        address = in.readString();
        total = in.readString();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        this.Uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phone);
        parcel.writeString(Uid);
        parcel.writeString(address);
        parcel.writeString(total);
    }
}
