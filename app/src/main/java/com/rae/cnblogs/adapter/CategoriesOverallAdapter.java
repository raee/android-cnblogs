package com.rae.cnblogs.adapter;

import android.support.annotation.Nullable;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by ChenRui on 2017/7/16 0016 23:48.
 */
public class CategoriesOverallAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

    public CategoriesOverallAdapter(@Nullable List<AbstractFlexibleItem> items) {
        super(items);
    }
}
