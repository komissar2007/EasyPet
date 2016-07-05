package com.pets.slavar.easypet;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.location.places.Place;
import com.pets.slavar.easypet.entities.Argument;
import com.pets.slavar.easypet.entities.PlaceDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Slavar on 7/3/2016.
 */
public class FetchResultDetailsWS extends AsyncTask<String, Void, PlaceDetails> {
    private final String BASE_URL = "https://maps.googleapis.com/maps/api/place/details/json";
    private String JSONString = "";
    private final String key = "key";
    final String language = "language";
    final String PLACEID = "placeid";

    @Override
    protected PlaceDetails doInBackground(String... params) {
        BufferedReader reader;
        InputStream inputStream;
        HttpURLConnection urlConnection = null;
        String placeId = params[0];

        Uri buildUri = Uri.parse(BASE_URL + "?").buildUpon()
                .appendQueryParameter(language, "iw")
                .appendQueryParameter(PLACEID, placeId)
                .appendQueryParameter(key, BuildConfig.GOOGLE_PLACES_API_KEY).build();


        try {
            URL url = new URL(buildUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }


            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            JSONString = buffer.toString();

        } catch (MalformedURLException e) {
            Log.d(this.getClass().getSimpleName(), "Malformed URL");
        } catch (IOException e) {
            Log.d(this.getClass().getSimpleName(), "failed to open URL");
        }


        return getResultsFromJSON(JSONString);
    }

    private PlaceDetails getResultsFromJSON(String jsonString) {
        final String RESULT = "result";
        final String FORMATTED_ADDRESS = "formatted_address";
        final String PHONE_NUMBER = "formatted_phone_number";
        final String RATING = "rating";
        final String WEBSITE = "website";
        String address = null;
        String phoneNumber = null;
        String website = null;
        String rating = null;
        PlaceDetails placeDetails = null;
        JSONObject resultsJson = null;
        try {
            resultsJson = new JSONObject(jsonString);

            JSONObject resultJson = resultsJson.getJSONObject(RESULT);
            Log.d("SLAVAR", resultJson.toString());
            if (!resultJson.isNull(FORMATTED_ADDRESS)) {
                address = resultJson.getString(FORMATTED_ADDRESS);
                Log.d("SLAVAR", address);
            }
            if (!resultJson.isNull(PHONE_NUMBER)) {
                phoneNumber = resultJson.getString(PHONE_NUMBER);
                Log.d("SLAVAR", phoneNumber);
            }

            if (!resultJson.isNull(RATING)) {
                rating = resultJson.getString(RATING);
                Log.d("SLAVAR", rating);
            }
            if (!resultJson.isNull(WEBSITE)) {
                website = resultJson.getString(WEBSITE);
                Log.d("SLAVAR", website);
            }

            placeDetails = new PlaceDetails(phoneNumber, rating, website, address);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return placeDetails;
    }
}
