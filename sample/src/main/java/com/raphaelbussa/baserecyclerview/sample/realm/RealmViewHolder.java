package com.raphaelbussa.baserecyclerview.sample.realm;

import android.view.View;
import android.widget.TextView;

import com.raphaelbussa.baserecyclerview.BaseViewHolder;
import com.raphaelbussa.baserecyclerview.sample.R;

/**
 * Created by rebus007 on 29/10/17.
 */

public class RealmViewHolder extends BaseViewHolder<ItemModel> {

    private TextView title;

    public RealmViewHolder(View itemView) {
        super(itemView);
        title = findViewById(R.id.title);
    }

    @Override
    public void onBindViewHolder(ItemModel data) {
        title.setText(data.getText());
    }

}
