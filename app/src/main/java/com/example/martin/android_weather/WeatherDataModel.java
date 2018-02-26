package com.example.martin.android_weather;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by martin on 08/02/18.
 */

public class WeatherDataModel {
    // Member variables that will hold relevant weather information
    private String mTemperature;
    private String mCity;
    private String mIconName;
    private int mCondition;

    // Create a WeatherDataModel from a JSON.
    // We will call this instead of the standard constructor.
    public static WeatherDataModel fromJson(JSONObject jsonObject){
        // JSON parsing can be risk so it is done inside of a try catch block
        try{
            WeatherDataModel weatherData = new WeatherDataModel();
            weatherData.mCity = jsonObject.getString("name");

            return  weatherData;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
