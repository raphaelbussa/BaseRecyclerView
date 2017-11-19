package com.raphaelbussa.baserecyclerview.realm;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.raphaelbussa.baserecyclerview.BaseAdapter;
import com.raphaelbussa.baserecyclerview.BaseViewHolder;

import java.util.Collection;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollection;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by rebus007 on 25/10/17.
 */

public class BaseRealmAdapter<D extends RealmObject, VH extends BaseViewHolder<D>> extends BaseAdapter<D, VH> {

    private final OrderedRealmCollectionChangeListener listener;
    @Nullable
    private OrderedRealmCollection<D> adapterData;

    public BaseRealmAdapter(@Nullable OrderedRealmCollection<D> adapterData) {
        this(adapterData, null, -1);
    }

    public BaseRealmAdapter(@Nullable OrderedRealmCollection<D> adapterData, Class<VH> viewHolderClass, int layout) {
        super(viewHolderClass, layout);
        this.adapterData = adapterData;
        this.listener = createListener();
    }

    @Override
    public void addAll(Collection<D> items) {
        throw new RuntimeException("method not available in BaseRealmAdapter");
    }

    @Override
    public void addAll(Collection<D> items, int index) {
        throw new RuntimeException("method not available in BaseRealmAdapter");
    }

    @Override
    public void add(D item) {
        throw new RuntimeException("method not available in BaseRealmAdapter");
    }

    @Override
    public void add(D item, int index) {
        throw new RuntimeException("method not available in BaseRealmAdapter");
    }

    @Override
    public void remove(D item) {
        throw new RuntimeException("method not available in BaseRealmAdapter");
    }

    @Override
    public void remove(int pos) {
        throw new RuntimeException("method not available in BaseRealmAdapter");
    }

    @Override
    public void clear(boolean notify) {
        throw new RuntimeException("method not available in BaseRealmAdapter");
    }

    @Override
    public void clear() {
        throw new RuntimeException("method not available in BaseRealmAdapter");
    }

    @Override
    public List<D> getData() {
        throw new RuntimeException("method not available in BaseRealmAdapter");
    }

    @SuppressWarnings("unchecked")
    private OrderedRealmCollectionChangeListener<RealmResults<D>> createListener() {
        return new OrderedRealmCollectionChangeListener() {
            @Override
            public void onChange(@NonNull Object ds, @Nullable OrderedCollectionChangeSet changeSet) {
                try {
                    getRecyclerView().getRecycledViewPool().clear();
                } catch (Exception ignored) {}

                if (changeSet == null) {
                    notifyDataSetChanged();
                    return;
                }

                OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                for (int i = deletions.length - 1; i >= 0; i--) {
                    OrderedCollectionChangeSet.Range range = deletions[i];
                    notifyItemRangeRemoved(getHeaderViewSize() + range.startIndex, range.length);
                }

                OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                for (OrderedCollectionChangeSet.Range range : insertions) {
                    notifyItemRangeInserted(getHeaderViewSize() + range.startIndex, range.length);
                }

                OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                for (OrderedCollectionChangeSet.Range range : modifications) {
                    notifyItemRangeChanged(getHeaderViewSize() + range.startIndex, range.length);
                }

            }
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, D item, int position) {
        holder.onBindViewHolder(getItem(position));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public D getItem(int index) {
        return isDataValid() ? adapterData.get(index) : null;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (isDataValid()) {
            addListener(adapterData);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (isDataValid()) {
            removeListener(adapterData);
        }
    }

    @SuppressWarnings("unchecked")
    private void addListener(@NonNull OrderedRealmCollection<D> data) {
        if (listener == null) return;
        if (data instanceof RealmResults) {
            RealmResults<D> results = (RealmResults<D>) data;
            results.addChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList<D> list = (RealmList<D>) data;
            list.addChangeListener(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    private void removeListener(@NonNull OrderedRealmCollection<D> data) {
        if (listener == null) return;
        if (data instanceof RealmResults) {
            RealmResults<D> results = (RealmResults<D>) data;
            results.removeChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList<D> list = (RealmList<D>) data;
            list.removeChangeListener(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int getDataSize() {
        return isDataValid() ? adapterData.size() : 0;
    }

    private boolean isDataValid() {
        return adapterData != null && adapterData.isValid();
    }

}
