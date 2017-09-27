package com.raphaelbussa.baserecyclerview.sample;

import android.view.View;
import android.widget.TextView;

import com.raphaelbussa.baserecyclerview.BaseViewHolder;

/**
 * Created by rebus007 on 02/09/17.
 */

public class MainViewHolder extends BaseViewHolder<MainActivity.Bean> {

    private TextView textView;

    public MainViewHolder(View itemView) {
        super(itemView);
        textView = findViewById(R.id.title);
    }

    @Override
    public void onBindViewHolder(MainActivity.Bean data) {
        textView.setText(data.getText());
    }

}
