package com.example.android.mykaarma.sharedPref;

import android.content.Context;

import com.example.android.mykaarma.ClassObjects.SharedprefObject;

public class SharedPreferences {

    SharedprefObject sharedprefObject;
    public static final String SHARED_PREF_NAME = "DOC";
    public static final String COMPANY_DETAILS_STRING = "COMPANY_DETAIL";
    public static final String USER_DETAILS_STRING = "USER_DETAIL";


    public void getDetails(Context context, SharedprefObject sharedprefObject) {
        this.sharedprefObject = sharedprefObject;
        SharedPreferences sharedPreferences = (SharedPreferences) context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
//        SharedPreferences.Editor = sharedPreferences.edit();
    }
}
