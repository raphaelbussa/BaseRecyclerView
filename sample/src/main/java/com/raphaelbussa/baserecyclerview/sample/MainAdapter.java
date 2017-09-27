package com.raphaelbussa.baserecyclerview.sample;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.raphaelbussa.baserecyclerview.BaseAdapter;

/**
 * Created by rebus007 on 02/09/17.
 */

public class MainAdapter extends BaseAdapter<MainActivity.Bean, MainViewHolder> {

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainViewHolder mainViewHolder = new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_test, parent, false));
        return mainViewHolder;
    }

}
