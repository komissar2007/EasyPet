package com.pets.slavar.easypet.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;


import com.pets.slavar.easypet.R;

/**
 * Created by Slavar on 7/17/2016.
 */
public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
    }
}
