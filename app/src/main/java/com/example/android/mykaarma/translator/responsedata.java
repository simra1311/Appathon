package com.example.android.mykaarma.translator;

/**
 * This interface is created to be initialised in translate class and used as callback in implementation of translate class response
 *
 * @author Vinod Kumar
 * @version 1.0
 */
public interface responsedata {

    /**
     * Response function used as callback by translate class and overrided to get response
     * @param word string response of translate class
     * @param position position of word
     */
    void response(String word, int position);
}