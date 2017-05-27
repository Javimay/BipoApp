package com.bipo.javier.bipo.login.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Javier on 12/05/2017.
 */

public class BikeBrand implements Parcelable{

    public int id;
    public String brand;

    public BikeBrand(Parcel in) {
        id = in.readInt();
        brand = in.readString();
    }

    public static final Creator<BikeBrand> CREATOR = new Creator<BikeBrand>() {
        @Override
        public BikeBrand createFromParcel(Parcel in) {
            return new BikeBrand(in);
        }

        @Override
        public BikeBrand[] newArray(int size) {
            return new BikeBrand[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(brand);
    }
}
