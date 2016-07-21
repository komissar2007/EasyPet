package com.pets.slavar.easypet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pets.slavar.easypet.R;
import com.pets.slavar.easypet.RecyclerListAdapter;
import com.pets.slavar.easypet.ResultsListArrayAdapter;
import com.pets.slavar.easypet.entities.Argument;
import com.pets.slavar.easypet.entities.Result;

/**
 * Created by SLAVAR on 6/11/2016.
 */
public class ResultsFragment extends Fragment {
    private Bundle bundle;
    private ResultsListArrayAdapter resultsListArrayAdapter;
    private RecyclerListAdapter recyclerListAdapter;
    private RecyclerView recRecyclerView;
    private Argument argument;
    private Result[] results;


    public ResultsFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        final Context con = getActivity();
        recyclerListAdapter = new RecyclerListAdapter(con, results, getFragmentManager().beginTransaction());
        recRecyclerView.setAdapter(recyclerListAdapter);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bundle = this.getArguments();
        argument = bundle.getParcelable("argument");
        results = (Result[]) bundle.getParcelableArray("result");
        Log.d("SLAVAR TIME 64:",String.valueOf(System.currentTimeMillis()));
        View view = inflater.inflate(R.layout.fragment_results, container, false);

        ((CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar_layout)).setTitle(argument.getCategory().getName());
        Log.d("SLAVAR TIME 68:",String.valueOf(System.currentTimeMillis()));
        recRecyclerView = (RecyclerView) view.findViewById(R.id.results_recycler_view);
        recRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Log.d("SLAVAR TIME 71:",String.valueOf(System.currentTimeMillis()));
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
