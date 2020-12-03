package com.example.adminbikerental.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Bike implements Parcelable {
    private String id, color, merk, price, code, image;

    public Bike() {

    }

    protected Bike(Parcel in) {
        id = in.readString();
        color = in.readString();
        merk = in.readString();
        price = in.readString();
        code = in.readString();
        image = in.readString();
    }

    public static final Creator<Bike> CREATOR = new Creator<Bike>() {
        @Override
        public Bike createFromParcel(Parcel in) {
            return new Bike(in);
        }

        @Override
        public Bike[] newArray(int size) {
            return new Bike[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        parcel.writeString(id);
        parcel.writeString(color);
        parcel.writeString(merk);
        parcel.writeString(price);
        parcel.writeString(code);
        parcel.writeString(image);
    }
}
