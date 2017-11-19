package com.raphaelbussa.baserecyclerview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rebus007 on 01/09/17.
 */

public class BaseAdapter<D, VH extends BaseViewHolder<D>> extends BaseRecyclerView.Adapter<BaseViewHolder> {

    private static final String TAG = BaseAdapter.class.getName();

    private static final int HEADERS_START = Integer.MIN_VALUE;
    private static final int FOOTERS_START = Integer.MIN_VALUE + 10;

    private Class<VH> viewHolderClass;
    private int layout;

    private List<View> headerViews;
    private List<View> footerViews;

    private List<D> data;
    private BaseAdapter.OnClickListener<D> onClickListener;
    private BaseAdapter.OnLongClickListener<D> onLongClickListener;
    private BaseAdapter.OnFocusChangeListener<D> onFocusChangeListener;

    private SparseArrayCompat<BaseAction.Bean> actions;

    private RecyclerView recyclerView;

    public BaseAdapter() {
        this(null, -1);
    }

    public BaseAdapter(Class<VH> viewHolderClass, int layout) {
        this.setHasStableIds(true);
        this.data = new ArrayList<>();
        this.viewHolderClass = viewHolderClass;
        this.layout = layout;
        this.headerViews = new ArrayList<>();
        this.footerViews = new ArrayList<>();
        this.actions = new SparseArrayCompat<>();
    }

    public void addHeader(ViewGroup parent, @LayoutRes int header) {
        View view = LayoutInflater.from(parent.getContext()).inflate(header, parent, false);
        addHeader(view);
    }

    public void addHeader(View header) {
        this.headerViews.add(header);
    }

    public void addFooter(ViewGroup parent, @LayoutRes int footer) {
        View view = LayoutInflater.from(parent.getContext()).inflate(footer, parent, false);
        addFooter(view);
    }

    public void addFooter(View footer) {
        this.footerViews.add(footer);
    }

    public void addAll(Collection<D> items) {
        int pos = getDataSize() + headerViews.size();
        this.data.addAll(items);
        this.notifyItemRangeInserted(pos, items.size());
    }

    public void addAll(Collection<D> items, int index) {
        this.data.addAll(index, items);
        this.notifyItemRangeInserted(index + headerViews.size(), items.size());
    }

    public void add(D item) {
        this.data.add(item);
        this.notifyItemInserted(getDataSize() + headerViews.size() - 1);
    }

    public void add(D item, int index) {
        this.data.add(index, item);
        this.notifyItemInserted(index + headerViews.size());
    }

    public void remove(D item) {
        int pos = data.indexOf(item);
        this.data.remove(item);
        this.notifyItemRemoved(pos + headerViews.size());
    }

    public void remove(int pos) {
        this.data.remove(pos);
        this.notifyItemRemoved(pos + headerViews.size());
    }

    public void clear(boolean notify) {
        int size = getDataSize();
        this.data.clear();
        if (notify) this.notifyItemRangeRemoved(headerViews.size(), size);
    }

    public void clear() {
        clear(true);
    }

    public List<D> getData() {
        return data;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType < HEADERS_START + headerViews.size())
            return new StaticViewHolder(headerViews.get(viewType - HEADERS_START));
        else if (viewType < FOOTERS_START + footerViews.size())
            return new StaticViewHolder(footerViews.get(viewType - FOOTERS_START));
        else {
            if (viewHolderClass == null) {
                BaseViewHolder baseViewHolder = onCreateBaseViewHolder(parent, viewType);
                if (baseViewHolder == null) {
                    throw new RuntimeException("if you use empty constructor, you must override onCreateBaseViewHolder");
                }
                return baseViewHolder;
            }
            try {
                Constructor<VH> constructor = viewHolderClass.getConstructor(View.class);
                return constructor.newInstance(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
            } catch (Exception e) {
                return null;
            }
        }
    }

    public BaseViewHolder onCreateBaseViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @SuppressWarnings("unchecked")
    public void onBindViewHolder(BaseViewHolder holder, D item, int position) {
        holder.onBindViewHolder(item);
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, int pos) {
        if (pos >= headerViews.size() && pos < headerViews.size() + getDataSize()) {
            int position = pos - headerViews.size();
            if (onClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onClick(View view) {
                        int realPosition = holder.getAdapterPosition() - headerViews.size();
                        onClickListener.onClick(view, getItem(realPosition), realPosition);
                    }
                });
            }
            if (onLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public boolean onLongClick(View view) {
                        int realPosition = holder.getAdapterPosition() - headerViews.size();
                        return onLongClickListener.onLongClick(view, getItem(realPosition), realPosition);
                    }
                });
            }
            if (onFocusChangeListener != null) {
                holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onFocusChange(View view, boolean hasFocus) {
                        int realPosition = holder.getAdapterPosition() - headerViews.size();
                        onFocusChangeListener.onFocusChange(view, hasFocus, getItem(realPosition), realPosition);
                    }
                });
            }
            onBindViewHolder(holder, getItem(position), position);
        }
    }

    @Override
    public void onViewRecycled(BaseViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < headerViews.size()) {
            return HEADERS_START + position;
        } else {
            int itemCount = getDataSize();
            if (position < headerViews.size() + itemCount) {
                int realPosition = position - headerViews.size();
                return getItemViewType(getItem(realPosition), realPosition);
            } else {
                return FOOTERS_START + position - headerViews.size() - itemCount;
            }
        }
    }

    public int getItemViewType(D item, int position) {
        return 0;
    }

    public void addAction(@DrawableRes int icon, @ColorInt int color, @BaseAction.Direction int swipeDirection) {
        actions.put(swipeDirection, new BaseAction.Bean(swipeDirection, icon, color));
    }

    @Override
    public int getItemCount() {
        return headerViews.size() + getDataSize() + footerViews.size();
    }

    public void setOnClickListener(OnClickListener<D> onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener<D> onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setOnFocusChangeListener(OnFocusChangeListener<D> onFocusChangeListener) {
        this.onFocusChangeListener = onFocusChangeListener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        super.onAttachedToRecyclerView(recyclerView);
        if (actions.size() != 0) {
            addActions(recyclerView);
        }
    }

    private void addActions(RecyclerView recyclerView) {
        int swipe = 0;
        for (int i = 0; i < actions.size(); i++) {
            swipe += actions.keyAt(i);
        }
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, swipe) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                notifyItemChanged(position);
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                if (viewHolder instanceof StaticViewHolder) return 0;
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    if (dX > 0) {
                        Log.i(TAG, "onChildDraw: RIGHT dX " + dX);
                        createAction(c, actions.get(ItemTouchHelper.RIGHT), viewHolder.itemView, dX + recyclerView.getPaddingRight());
                    } else if (dX < 0) {
                        Log.i(TAG, "onChildDraw: LEFT dX " + dX);
                        createAction(c, actions.get(ItemTouchHelper.LEFT), viewHolder.itemView, dX - recyclerView.getPaddingRight());
                    }
                    /*if (dY > 0) {
                        //down
                        createAction(c, actions.get(ItemTouchHelper.DOWN), viewHolder.itemView, dY);
                    } else {
                        //up
                        createAction(c, actions.get(ItemTouchHelper.UP), viewHolder.itemView, dY);
                    }*/
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void createAction(Canvas canvas, BaseAction.Bean bean, View itemView, float delta) {
        if (bean == null) return;
        RectF rectF = null;
        RectF background = null;
        float size;
        switch (bean.getDirection()) {
            case ItemTouchHelper.RIGHT:
                size = ((float) itemView.getBottom() - (float) itemView.getTop()) / 3;
                rectF = new RectF((float) itemView.getLeft() + size, (float) itemView.getTop() + size, (float) itemView.getLeft() + 2 * size, (float) itemView.getBottom() - size);
                background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), delta, (float) itemView.getBottom());
                break;
            case ItemTouchHelper.LEFT:
                size = ((float) itemView.getBottom() - (float) itemView.getTop()) / 3;
                rectF = new RectF((float) itemView.getRight() - 2 * size, (float) itemView.getTop() + size, (float) itemView.getRight() - size, (float) itemView.getBottom() - size);
                background = new RectF((float) itemView.getRight() + delta, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                break;
            case ItemTouchHelper.UP:
                size = (float) itemView.getRight() - (float) itemView.getLeft() / 3;
                rectF = new RectF((float) itemView.getRight() - 2 * size, (float) itemView.getTop() + size, (float) itemView.getRight() - size, (float) itemView.getBottom() - size);
                break;
            case ItemTouchHelper.DOWN:
                size = (float) itemView.getRight() - (float) itemView.getLeft() / 3;
                rectF = new RectF((float) itemView.getRight() - 2 * size, (float) itemView.getTop() + size, (float) itemView.getRight() - size, (float) itemView.getBottom() - size);
                break;
        }
        if (rectF == null || background == null) return;
        Paint paint = new Paint();
        paint.setColor(bean.getColor());
        canvas.drawRect(background, paint);
        if (bean.getIcon() == 0) return;
        Bitmap bitmap = BitmapFactory.decodeResource(itemView.getResources(), bean.getIcon());
        canvas.drawBitmap(bitmap, null, rectF, paint);
    }

    public D getItem(int index) {
        return data.get(index);
    }

    public int getDataSize() {
        return data.size();
    }

    public int getHeaderViewSize() {
        return headerViews.size();
    }

    public int getFooterViewSize() {
        return footerViews.size();
    }

    public interface OnLongClickListener<D> {
        boolean onLongClick(View view, D item, int position);
    }

    public interface OnClickListener<D> {
        void onClick(View view, D item, int position);
    }

    public interface OnFocusChangeListener<D> {
        void onFocusChange(View view, boolean hasFocus, D item, int position);
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }



}
