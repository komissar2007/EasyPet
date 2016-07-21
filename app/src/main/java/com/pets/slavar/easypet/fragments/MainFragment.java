package com.pets.slavar.easypet.fragments;


import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.pets.slavar.easypet.CategoryGridAdapter;
import com.pets.slavar.easypet.FetchResultsFromWS;
import com.pets.slavar.easypet.activities.MainActivity;
import com.pets.slavar.easypet.R;
import com.pets.slavar.easypet.entities.Argument;
import com.pets.slavar.easypet.entities.Category;
import com.pets.slavar.easypet.entities.Coordinates;
import com.pets.slavar.easypet.entities.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private GridView categoryGridVIew;
    private FragmentTransaction fragmentTransaction;
    private Coordinates coordinates = null;
    private TextView sourceLocationTextView;
    private Address address;


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        ArrayAdapter adapter = new CategoryGridAdapter(getContext(), DummyData.createDummyCategories(getActivity()));
        categoryGridVIew.setAdapter(adapter);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            coordinates = savedInstanceState.getParcelable(getString(R.string.coordinates));
            address = savedInstanceState.getParcelable(getString(R.string.address));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        if (savedInstanceState == null) {
            coordinates = ((MainActivity) getActivity()).getCoordinates();
        }
        address = findCurrentLocation();
        fragmentTransaction = getFragmentManager().beginTransaction();
        categoryGridVIew = (GridView) view.findViewById(R.id.gridView);
        setLocationTextView(view, address);
        Log.d("SLAVAR TIME 79:",String.valueOf(System.currentTimeMillis()));
        setGridViewListener();
        Log.d("SLAVAR TIME 81:",String.valueOf(System.currentTimeMillis()));
        return view;
    }

    private void setLocationTextView(View view, Address address) {
        sourceLocationTextView = (TextView) view.findViewById(R.id.source_location_text_view);
        int maxAddressLines = address.getMaxAddressLineIndex();
        String addressText = "";
        if (maxAddressLines > 0 && maxAddressLines >= 2) {
            addressText = address.getAddressLine(0) + ", " + address.getAddressLine(1);
        } else if (maxAddressLines == 1) {
            addressText = address.getAddressLine(0);
        }

        sourceLocationTextView.setText(addressText);
    }

    private Address findCurrentLocation() {
        List<Address> list = new ArrayList<>();
        try {
            Geocoder geocoder = new Geocoder(getActivity(), new Locale("iw"));


            list = geocoder.getFromLocation(Double.parseDouble(coordinates.getLatitude()), Double.parseDouble(coordinates.getLongitude()), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list.get(0);
    }

    private void setGridViewListener() {
        categoryGridVIew.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (coordinates.isValid()) {
                    Log.d("SLAVAR TIME 114:",String.valueOf(System.currentTimeMillis()));
                    Category category = (Category) categoryGridVIew.getAdapter().getItem(position);
                    ResultsFragment resultsFragment = new ResultsFragment();
                    Bundle bundle = new Bundle();

                    bundle.putParcelable(getResources().getString(R.string.category), category);
                    bundle.putString("latitude", coordinates.getLatitude());
                    bundle.putString("longitude", coordinates.getLongitude());
                    Argument argument = new Argument();
                    argument.setCategory(category);
                    argument.setCoordinates(coordinates);
                    bundle.putParcelable("argument", argument);
                    resultsFragment.setArguments(bundle);
                    Log.d("SLAVAR TIME 129:",String.valueOf(System.currentTimeMillis()));
                    FetchResultsFromWS fetchResultsFromWS = new FetchResultsFromWS();
                    fetchResultsFromWS.execute(argument);

                    try {
                        Result[] result = fetchResultsFromWS.get();
                        Log.d("SLAVAR TIME 133:",String.valueOf(System.currentTimeMillis()));

                        if (result == null)
                        {
                            Toast.makeText(getActivity(), getString(R.string.error_connect_to_server), Toast.LENGTH_SHORT).show();
                        }
                        else if
                        ((result.length > 0)) {
                            bundle.putParcelableArray("result", result);
                            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .replace(R.id.fragment_container, resultsFragment)
                                    .addToBackStack("null").commit();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.no_results_found_text), Toast.LENGTH_SHORT).show();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }


                } else {
                    Toast.makeText(getActivity(), "Cannot allocate current location...", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.coordinates), coordinates);
        outState.putParcelable(getString(R.string.address), address);
    }
}
