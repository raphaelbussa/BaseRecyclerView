package com.raphaelbussa.baserecyclerview;

import android.content.Context;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by rebus007 on 01/09/17.
 */

public abstract class BaseViewHolder<D> extends BaseRecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
        init();
    }

    private void init() {
        if (itemView.getBackground() == null) {
            itemView.setBackgroundResource(Utils.selectableItemBackground(getContext()));
        }
    }

    public Context getContext() {
        return itemView == null ? null : itemView.getContext();
    }

    public <V extends View> V findViewById(@IdRes int id) {
        return itemView.findViewById(id);
    }

    public abstract void onBindViewHolder(D data);

}
