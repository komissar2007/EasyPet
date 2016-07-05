package com.pets.slavar.easypet;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pets.slavar.easypet.entities.Result;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

/**
 * Created by Slavar on 6/16/2016.
 */
public class ResultsListArrayAdapter extends ArrayAdapter<Result> {
    private final LayoutInflater inflater;
    private Result[] resultArray;
    private Context context;
    TextView nameTextView;
    TextView addressTextView;
    ImageView resultImage;
    TextView distanceTextView;

    public ResultsListArrayAdapter(Context context, Result[] results) {
        super(context, R.layout.result_list_item, results);

        inflater = LayoutInflater.from(context);
        resultArray = results;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.result_list_item, parent, false);
        }
        nameTextView = (TextView) convertView.findViewById(R.id.result_list_item_label);
        nameTextView.setText(resultArray[position].getName());

        addressTextView = (TextView) convertView.findViewById(R.id.result_list_item_address);
        addressTextView.setText(resultArray[position].getAddress());


        distanceTextView = (TextView) convertView.findViewById(R.id.result_list_item_distance);
        //distanceTextView.setText(String.valueOf(resultArray[position].getDistance()).concat(" מ'"));
        String distance = new DecimalFormat("##.##").format(resultArray[position].getDistance());
        distanceTextView.setText(distance.concat(" קמ"));


        resultImage = (ImageView) convertView.findViewById(R.id.result_list_item_image);
        Picasso.with(context).load(resultArray[position].getImageUrl()).fit().into(resultImage);
        return convertView;
    }


}
