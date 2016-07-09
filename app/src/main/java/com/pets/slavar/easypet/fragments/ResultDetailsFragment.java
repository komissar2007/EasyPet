package com.pets.slavar.easypet.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.places.Place;
import com.pets.slavar.easypet.FetchResultDetailsWS;
import com.pets.slavar.easypet.FetchResultsFromWS;
import com.pets.slavar.easypet.R;
import com.pets.slavar.easypet.RecyclerListAdapter;
import com.pets.slavar.easypet.entities.Argument;
import com.pets.slavar.easypet.entities.PlaceDetails;
import com.pets.slavar.easypet.entities.Result;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class ResultDetailsFragment extends Fragment {

    private ImageButton navigateButton;
    private TextView phoneNumberTextView;
    private TextView websiteTextView;
    Result result;


    public ResultDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        FetchResultDetailsWS fetchResult = new FetchResultDetailsWS();
        fetchResult.execute(result.getPlace_id());
        try {
            PlaceDetails placeDetails = fetchResult.get();
            populateDetailsLayout(placeDetails);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void populateDetailsLayout(PlaceDetails placeDetails) {
        phoneNumberTextView.setText(placeDetails.getPhoneNumber());
        websiteTextView.setText(placeDetails.getWebSite());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result_details, container, false);
        Bundle bundle = this.getArguments();
        result = bundle.getParcelable(ResultDetailsFragment.class.getSimpleName());
        ((CollapsingToolbarLayout) view.findViewById(R.id.result_details_collapsing_toolbar_layout)).setTitle(result.getName());
        ImageView headerImageView = (ImageView) view.findViewById(R.id.result_details_header_image);
        if (!result.getImageUrl().startsWith(getString(R.string.file_prefix)))
        {
            Picasso.with(getActivity()).load(result.getImageUrl()).fit().into(headerImageView);
        }


        navigateButton = (ImageButton) view.findViewById(R.id.navigate_result_details);
        navigateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uriBegin = "geo:" + result.getCoordinates().getLatitude() + "," + result.getCoordinates().getLongitude();
                String query = result.getCoordinates().getLatitude() + "," + result.getCoordinates().getLongitude();
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery;
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        phoneNumberTextView = (TextView) view.findViewById(R.id.dial_result_details_text);
        websiteTextView = (TextView) view.findViewById(R.id.website_result_details_text);
        return view;
    }
}
