package com.pets.slavar.easypet.fragments;

import android.content.Context;

import com.pets.slavar.easypet.R;
import com.pets.slavar.easypet.entities.Category;

/**
 * Created by SLAVAR on 6/9/2016.
 */
public class DummyData {
    static Category[] createDummyCategories(Context context) {
        String categoryNames[] = context.getResources().getStringArray(R.array.categories);
        String categoryImages[] = context.getResources().getStringArray(R.array.categories_icons);
        Category[] categoryArray = new Category[0];
        if (categoryNames.length > 0) {
            categoryArray = new Category[categoryNames.length];
            for (int i = 0; i < categoryNames.length; i++) {
                categoryArray[i] = new Category();
                categoryArray[i].setImageUrl(categoryImages[i]);
                categoryArray[i].setName(categoryNames[i]);
            }

    }
        return categoryArray;
    }
}
