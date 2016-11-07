package com.catecyruseduardoginatori.restaurantrecommender;


import android.os.AsyncTask;
import android.util.Log;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * Created by cyrus on 11/5/16.
 */

public class YelpConnector extends AsyncTask<Double, Void, ArrayList<Business>> {

    @Override
    protected ArrayList<Business> doInBackground(Double... coordinates) {
        // Longitude and latitude indexes in array
        final int LATITUDE            = 0;
        final int LONGITUDE           = 1;

        // Yelp API keys
        final String CONSUMER_KEY     = "kMlUk7j_mjSlpAOYajl-HA";
        final String CONSUMER_SECRET  = "wdn7aNfEIeXbO0FMsLstJ291UkQ";
        final String TOKEN            = "2js99sf6NqeOKkB2lnowe3Rg0lXQJkPD";
        final String TOKEN_SECRET     = "OLOqOs267LaQMEAu9NDMaq7Dd9s";

        // Create instance of Yelp's android api
        YelpAPIFactory apiFactory
                = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        YelpAPI yelpAPI = apiFactory.createAPI();
        Map<String, String> params = new HashMap<>();

        // Add parameters
        params.put("category", "restaurants");
        params.put("sort", "1");
        params.put("lang", "en");
        params.put("is_closed", "false");

        // Create Yelp cooridates using user's last known gps location
        CoordinateOptions coordinate = CoordinateOptions.builder()
                .latitude(coordinates[LATITUDE])
                .longitude(coordinates[LONGITUDE])
                .build();

        // Call Yelp API search and prepare ArrayList of businesses as results
        Call<SearchResponse> call = yelpAPI.search(coordinate, params);
        ArrayList<Business> businesses = null;
        try {
            SearchResponse searchResponse = call.execute().body();
            int totalNumberOfResults =  searchResponse.businesses().size();
            businesses = searchResponse.businesses();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return list of businesses
        return businesses;
    }

    @Override
    protected void onPostExecute(ArrayList<Business> businesses) {
        if (businesses != null) {
            for (int i = 0; i < businesses.size(); i++) {
                Log.i("BUSINESS", businesses.get(i).name());
            }
        } else {
            Log.i("BUSINESS", "Was null");
        }
    }
}

