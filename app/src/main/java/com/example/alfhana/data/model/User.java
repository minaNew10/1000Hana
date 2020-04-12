package com.example.alfhana.data.model;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


public class User implements Parcelable{

    public String displayName;
    public String email;
    public String phone;
    public String address;
    public String image;
    LocationHelper locationHelper;

    public User(String name, String email, String phone, String address) {
        this.displayName = name;
        this.email = email;
        this.phone = phone;
        this.address = address;

    }


    protected User(Parcel in) {
        displayName = in.readString();
        email = in.readString();
        phone = in.readString();
        address = in.readString();
        image = in.readString();
        locationHelper = in.readParcelable(LocationHelper.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public LocationHelper getLocationHelper() {
        return locationHelper;
    }

    public void setLocationHelper(LocationHelper locationHelper) {
        this.locationHelper = locationHelper;
    }

    //required to prevent error
    public User() {
    }


    public User(String displayName, String email, String phone, String address,LocationHelper locationHelper) {
        this.displayName = displayName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.locationHelper = locationHelper;
        image =null;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @BindingAdapter("profileImage")
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl).apply(new RequestOptions().circleCrop())
                .into(view);
    }

    public String getDisplayName() {
        return displayName;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(displayName);
        parcel.writeString(email);
        parcel.writeString(phone);
        parcel.writeString(address);
        parcel.writeString(image);
        parcel.writeParcelable(locationHelper, i);
    }
}
