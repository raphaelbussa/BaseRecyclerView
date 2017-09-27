package com.raphaelbussa.baserecyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 * Created by rebus007 on 01/09/17.
 */

@SuppressWarnings("ALL")
public class BaseRecyclerView extends RecyclerView {

    private LayoutManager baseLayoutManager;

    private int layoutManager = 0;
    private int orientation = 0;
    private boolean reverseLayout = false;
    private boolean stackFromEnd = false;
    private int spanCount = 1;

    public BaseRecyclerView(Context context) {
        this(context, null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttr(attrs, defStyle);
        init();
    }

    private void initAttr(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BaseRecyclerView, defStyle, 0);
        layoutManager = typedArray.getInt(R.styleable.BaseRecyclerView_brw_layout_manager, 0);
        orientation = typedArray.getInt(R.styleable.BaseRecyclerView_brw_orientation, 0);
        reverseLayout = typedArray.getBoolean(R.styleable.BaseRecyclerView_brw_reverse_layout, false);
        stackFromEnd = typedArray.getBoolean(R.styleable.BaseRecyclerView_brw_stack_from_end, false);
        spanCount = typedArray.getInteger(R.styleable.BaseRecyclerView_brw_span_count, 1);
        typedArray.recycle();
    }

    private void init() {
        switch (layoutManager) {
            case 0:
                baseLayoutManager = new LinearLayoutManager(getContext(), orientation, reverseLayout);
                ((LinearLayoutManager) baseLayoutManager).setStackFromEnd(stackFromEnd);
                break;
            case 1:
                baseLayoutManager = new GridLayoutManager(getContext(), spanCount, orientation, reverseLayout);
                ((GridLayoutManager) baseLayoutManager).setStackFromEnd(stackFromEnd);
                break;
            case 2:
                baseLayoutManager = new StaggeredGridLayoutManager(spanCount, orientation);
                ((StaggeredGridLayoutManager) baseLayoutManager).setReverseLayout(reverseLayout);
                break;
            default:
                baseLayoutManager = new LinearLayoutManager(getContext());
                break;
        }
        setLayoutManager(baseLayoutManager);
    }

}
