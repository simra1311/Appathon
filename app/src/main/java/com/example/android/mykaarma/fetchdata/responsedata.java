package com.example.android.mykaarma.fetchdata;

import org.json.JSONObject;

/**
 *This is a responsedata interface initialised by fetch class and overrided by any class using fetch class to get response data
 *
 * @author Vinod Kumar
 * @version 1.0
 */

public interface responsedata {

    /**
     * Response function used as callback by fetch class and overrided to give response
     * @param data Json data
     */
    void response(JSONObject data);
}

