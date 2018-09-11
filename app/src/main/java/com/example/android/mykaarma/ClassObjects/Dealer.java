package com.example.android.mykaarma.ClassObjects;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Dealer implements Serializable{
    public  String ID;
    public  String address,email,dealeruserID,priceInINR,rating;
    public static LatLng latLng;

    public Dealer(String ID, String email, String dealeruserID,String priceInINR,String rating, LatLng latLng) {
        this.ID = ID;
        this.email = email;
        Dealer.latLng = latLng;
        this.dealeruserID = dealeruserID;
        this.priceInINR = priceInINR;
        this.rating = rating;
    }

//    public Dealer(int id){
//        this.ID = id;
//    }

    public Dealer(String ID,String address) {
        this.ID = ID;
        this.address = address;
    }

    public String getAddress(Context context, double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        address = result.toString();
        return result.toString();
    }

}
