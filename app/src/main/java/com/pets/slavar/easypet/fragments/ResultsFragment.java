package com.pets.slavar.easypet.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pets.slavar.easypet.FetchResultsFromWS;
import com.pets.slavar.easypet.MainActivity;
import com.pets.slavar.easypet.R;
import com.pets.slavar.easypet.RecyclerListAdapter;
import com.pets.slavar.easypet.ResultsListArrayAdapter;
import com.pets.slavar.easypet.entities.Argument;
import com.pets.slavar.easypet.entities.Result;

import java.util.concurrent.ExecutionException;

/**
 * Created by SLAVAR on 6/11/2016.
 */
public class ResultsFragment extends Fragment {
    private Bundle bundle;
    private ResultsListArrayAdapter resultsListArrayAdapter;
    private RecyclerListAdapter recyclerListAdapter;
    private RecyclerView recRecyclerView;
    private Argument argument;


    public ResultsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        final Context con = getActivity();
        FetchResultsFromWS fetchResult = new FetchResultsFromWS();
        fetchResult.execute(argument);
        Result[] results;
        try {

            results = fetchResult.get();
            if (results.length != 0) {
                recyclerListAdapter = new RecyclerListAdapter(con, results, getFragmentManager().beginTransaction());
                recRecyclerView.setAdapter(recyclerListAdapter);
            } else {
                getFragmentManager().popBackStack();
                Toast.makeText(getActivity(), getString(R.string.no_results_found_text), Toast.LENGTH_LONG).show();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bundle = this.getArguments();
        argument = bundle.getParcelable("argument");
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        ((CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout)).setTitle(argument.getCategory().getName());
        recRecyclerView = (RecyclerView) view.findViewById(R.id.results_recycler_view);
        recRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
