package com.pets.slavar.easypet;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.pets.slavar.easypet.entities.Argument;
import com.pets.slavar.easypet.entities.Coordinates;
import com.pets.slavar.easypet.entities.Result;
import com.pets.slavar.easypet.fragments.MainFragment;
import com.pets.slavar.easypet.fragments.ResultsFragment;

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
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by SLAVAR on 6/12/2016.
 */
public class FetchResultsFromWS extends AsyncTask<Argument, String, Result[]> {


    private String JSONString = "";
    Result[] resultArray;
    Coordinates currentCoordinates;
    final String BASE_URL = "https://maps.googleapis.com/maps/api/place/search/json";
    final String PHOTOS_URL = "https://maps.googleapis.com/maps/api/place/photo";
    final String PHOTO_REFERENCE_PARAM = "photoreference";
    final String PHOTO_REFERENCE = "photo_reference";
    final String MAX_WIDTH = "maxwidth";
    final String types = "types";
    final String sortBy = "rankby";
    final String location = "location";
    final String sensor = "sensor";
    final String radius = "radius";
    final String name = "name";
    final String key = "key";
    final String language = "language";
    String defaultImage = "";

    public FetchResultsFromWS() {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Result[] results) {
        super.onPostExecute(results);
    }

    @Override
    protected Result[] doInBackground(Argument... params) {
        BufferedReader reader;
        InputStream inputStream;
        HttpURLConnection urlConnection = null;
        Argument argument = params[0];
        currentCoordinates = argument.getCoordinates();
        defaultImage = argument.getCategory().getImageUrl();


        Uri buildUri = Uri.parse(BASE_URL + "?").buildUpon()
                .appendQueryParameter(location, currentCoordinates.getLocation())
                .appendQueryParameter(sensor, "false")
                .appendQueryParameter("radius", "10000")
                .appendQueryParameter(language, "iw")
                .appendQueryParameter("name", argument.getCategory().getName())
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

    private Result[] getResultsFromJSON(String jsonString) {
        final String RESULTS = "results";
        final String GEOMETRY = "geometry";
        final String LOCATION = "location";
        final String LATITUDE = "lat";
        final String LONGITUDE = "lng";
        final String ICON = "icon";
        final String ID = "id";
        final String PLACE_ID = "place_id";
        final String NAME = "name";
        final String VICINITY = "vicinity";
        final String PHOTOS = "photos";
        final String HTML_ATTRIBUTIONS = "html_attributions";
        final String PHOTO_REFERENCE = "photo_reference";
        String referenceId = "null";


        Log.d(getClass().getSimpleName(), jsonString.toString());
        try {
            JSONObject resultsJson = new JSONObject(jsonString);
            JSONArray resultsJsonArray = resultsJson.getJSONArray(RESULTS);
            resultArray = new Result[resultsJsonArray.length()];

            for (int i = 0; i < resultsJsonArray.length(); i++) {
                String latitude = resultsJsonArray.getJSONObject(i).getJSONObject(GEOMETRY).getJSONObject(LOCATION).getString(LATITUDE);
                String longitude = resultsJsonArray.getJSONObject(i).getJSONObject(GEOMETRY).getJSONObject(LOCATION).getString(LONGITUDE);
                Coordinates destCoordinates = new Coordinates(latitude, longitude);
                String imageUrl = defaultImage;
                String id = resultsJsonArray.getJSONObject(i).getString(ID);
                String name = resultsJsonArray.getJSONObject(i).getString(NAME);
                String place_id = resultsJsonArray.getJSONObject(i).getString(PLACE_ID);
                String address = resultsJsonArray.getJSONObject(i).getString(VICINITY);
                imageUrl = setImageUrl(PHOTOS, PHOTO_REFERENCE, resultsJsonArray, i, imageUrl);
                resultArray[i] = new Result(destCoordinates, imageUrl, id, name, place_id, address, currentCoordinates);

            }
            Arrays.sort(resultArray, new Comparator<Result>() {
                @Override
                public int compare(Result lhs, Result rhs) {
                    return Float.compare(lhs.getDistance(), rhs.getDistance());
                    //return (int) (lhs.getDistance() - rhs.getDistance());
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultArray;
    }

    private String setImageUrl(String PHOTOS, String PHOTO_REFERENCE, JSONArray resultsJsonArray, int i, String imageUrl) throws JSONException {
        String referenceId;
        if (!resultsJsonArray.getJSONObject(i).isNull(PHOTOS)) {
            JSONArray photos = resultsJsonArray.getJSONObject(i).getJSONArray(PHOTOS);
            referenceId = photos.getJSONObject(0).getString(PHOTO_REFERENCE);
            imageUrl = retrievePhotoReference(referenceId);
        }
        return imageUrl;
    }

    private String retrievePhotoReference(String referenceId) {
        BufferedReader reader;
        InputStream inputStream;
        HttpURLConnection urlConnection = null;


        Uri buildUri = Uri.parse(PHOTOS_URL + "?").buildUpon()
                .appendQueryParameter(MAX_WIDTH, "400")
                .appendQueryParameter(PHOTO_REFERENCE_PARAM, referenceId)
                .appendQueryParameter(key, BuildConfig.GOOGLE_PLACES_API_KEY).build();

        return buildUri.toString();
    }
}
