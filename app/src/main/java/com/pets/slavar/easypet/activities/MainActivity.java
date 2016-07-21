package com.pets.slavar.easypet.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.pets.slavar.easypet.R;
import com.pets.slavar.easypet.entities.Coordinates;
import com.pets.slavar.easypet.fragments.MainFragment;
import com.pets.slavar.easypet.fragments.SettingsFragment;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final int MY_LOCATION_PERMISSION = 123;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Toolbar toolbar;



    private static String localLanguage;
    SharedPreferences prefs ;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);



        if (!isConnected(this)) {
            Toast.makeText(this, "No Network", Toast.LENGTH_SHORT).show();
            finish();
        }
        buildGoogleApiClient();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        } else
            Toast.makeText(this, "Not connected...", Toast.LENGTH_SHORT).show();

    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onConnected(Bundle bundle) {

        if (isNetworkAvailable()) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION_PERMISSION);
            } else {
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                commitFragment();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
      //  super.onSaveInstanceState(outState);
    }

    private void commitFragment() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(MainFragment.class.getSimpleName());
        if (currentFragment == null) {
            getSupportFragmentManager()
                    .beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.fragment_container, new MainFragment(), MainFragment.class.getSimpleName()).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_LOCATION_PERMISSION: {
                if (!((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED))) {
                    Toast.makeText(this, "Location permission are required...", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }
                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    commitFragment();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection suspended...", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Toast.makeText(this, "Failed to connect..." + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
        connectionResult.getErrorMessage();

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public Coordinates getCoordinates() {
        Coordinates coordinates;
        if (mLastLocation != null) {
            coordinates = new Coordinates(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));
        } else {
            return null;
        }
        return coordinates;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==R.id.action_settings)
        {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();



        if (prefs.getBoolean("firstrun", true)) {
            prefs.edit().putBoolean("firstrun", false).commit();
              AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Language");

                builder.setMessage("Choose Your Language");
                builder.setPositiveButton("Hebrew", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.edit().putString(getString(R.string.pref_language_key), getString(R.string.pref_units_hebrew_value)).commit();
                    }
                });
                builder.setNegativeButton("English", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        prefs.edit().putString(getString(R.string.pref_language_key), getString(R.string.pref_units_english_value)).commit();
                    }
                });
                builder.show();
        }
        String languange = prefs.getString(getString(R.string.pref_language_key),getString(R.string.pref_units_default));
        if (!languange.equals(getString(R.string.pref_units_default)))
        {
            setLocalLanguage(languange);
            Locale locale = new Locale(getLocalLanguage());
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            if ((getBaseContext().getResources().getConfiguration().locale != null)&&(!getBaseContext().getResources().getConfiguration().locale.getLanguage().equals(languange)))
            {
                finish();
                startActivity(new Intent(this,MainActivity.class));
            }
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public static void setLocalLanguage(String language) {
        MainActivity.localLanguage = language;
    }

    public static String getLocalLanguage() {
        return localLanguage;
    }
}
