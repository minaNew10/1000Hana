package com.example.alfhana.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "request")
public class Request implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long orderId;
    public String phone;
    public String name;
    public String address;
    public String total;
    public List<Order> orders;


    public Request() {
    }

    public Request(String phone, String name, String address, String total, List<Order> orders) {
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.orders = orders;
    }

    protected Request(Parcel in) {
        phone = in.readString();
        name = in.readString();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(phone);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(total);
    }
}
