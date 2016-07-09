package com.pets.slavar.easypet;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pets.slavar.easypet.entities.Result;
import com.pets.slavar.easypet.fragments.ResultDetailsFragment;
import com.pets.slavar.easypet.fragments.ResultsFragment;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

/**
 * Created by SLAVAR on 6/28/2016.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ResultViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    Result[] resultArray;
    FragmentTransaction fragmentTransaction;

    public RecyclerListAdapter(Context context, Result[] results, FragmentTransaction fragmentTransaction) {
        this.context = context;
        resultArray = results;
        inflater = LayoutInflater.from(this.context);
        this.fragmentTransaction = fragmentTransaction;
    }


    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.result_list_item, parent, false);
        ResultViewHolder viewHolder = new ResultViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {


        holder.nameTextView.setText(resultArray[position].getName());
        holder.addressTextView.setText(resultArray[position].getAddress());
        String distance = new DecimalFormat("##.## ").format(resultArray[position].getDistance());
        holder.distanceTextView.setText(distance.concat(context.getString(R.string.metric)));
        Picasso.with(context).load(resultArray[position].getImageUrl()).fit().into(holder.resultImage);

    }


    @Override
    public int getItemCount() {
        return resultArray.length;
    }

    class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView addressTextView;
        ImageView resultImage;
        TextView distanceTextView;


        public ResultViewHolder(View itemView) {

            super(itemView);
            distanceTextView = (TextView) itemView.findViewById(R.id.result_list_item_distance);
            resultImage = (ImageView) itemView.findViewById(R.id.result_list_item_image);
            nameTextView = (TextView) itemView.findViewById(R.id.result_list_item_label);
            addressTextView = (TextView) itemView.findViewById(R.id.result_list_item_address);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Result result = resultArray[getAdapterPosition()];
            Bundle bundle = new Bundle();
            bundle.putParcelable(ResultDetailsFragment.class.getSimpleName(),result);
            ResultDetailsFragment detailsFragment = new ResultDetailsFragment();
            detailsFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.fragment_container, detailsFragment,
                    ResultDetailsFragment.class.getSimpleName()).addToBackStack(null).commit();
        }
    }
}
