package com.pets.slavar.easypet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pets.slavar.easypet.entities.Category;
import com.squareup.picasso.Picasso;

/**
 * Created by SLAVAR on 6/9/2016.
 */
public class CategoryGridAdapter extends ArrayAdapter {
    Context context;
    Category[] categories;
    ImageView categoryImage;
    private TextView categoryLabel;

    private final LayoutInflater inflater;


    public CategoryGridAdapter(Context context, Category[] categories) {
        super(context, R.layout.grid_item, categories);
        this.categories = categories;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            categoryImage = (ImageView) convertView.findViewById(R.id.category_imageView);
            categoryLabel = (TextView) convertView.findViewById(R.id.category_label);
            Picasso.with(context).load(categories[position].getImageUrl()).fit().into((categoryImage));
            categoryLabel.setText(categories[position].getName());
        }

        return convertView;
    }
}