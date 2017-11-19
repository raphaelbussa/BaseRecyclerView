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

    private final boolean hasAutoUpdates;
    private final boolean updateOnModification;
    private final OrderedRealmCollectionChangeListener listener;
    @Nullable
    private OrderedRealmCollection<D> adapterData;

    public BaseRealmAdapter(@Nullable OrderedRealmCollection<D> data, boolean autoUpdate, boolean updateOnModification) {
        this(null, -1, data, autoUpdate, updateOnModification);
    }

    public BaseRealmAdapter(Class<VH> viewHolderClass, int layout, @Nullable OrderedRealmCollection<D> data, boolean autoUpdate, boolean updateOnModification) {
        super(viewHolderClass, layout);
        if (data != null && !data.isManaged()) {
            throw new IllegalStateException("Only use this adapter with managed RealmCollection, for un-managed lists you can just use the BaseRecyclerViewAdapter");
        }
        this.adapterData = data;
        this.hasAutoUpdates = autoUpdate;
        this.listener = hasAutoUpdates ? createListener() : null;
        this.updateOnModification = updateOnModification;
    }

    private OrderedRealmCollectionChangeListener createListener() {
        return new OrderedRealmCollectionChangeListener() {
            @Override
            public void onChange(@NonNull Object collection, OrderedCollectionChangeSet changeSet) {
                // null Changes means the async query returns the first time.
                if (changeSet == null) {
                    notifyDataSetChanged();
                    return;
                }
                // For deletions, the adapter has to be notified in reverse order.
                OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                for (int i = deletions.length - 1; i >= 0; i--) {
                    OrderedCollectionChangeSet.Range range = deletions[i];
                    notifyItemRangeRemoved(getHeaderViewSize() + range.startIndex, range.length);
                }

                OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                for (OrderedCollectionChangeSet.Range range : insertions) {
                    notifyItemRangeInserted(getHeaderViewSize() + range.startIndex, range.length);
                }

                if (!updateOnModification) {
                    return;
                }

                OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                for (OrderedCollectionChangeSet.Range range : modifications) {
                    notifyItemRangeChanged(getHeaderViewSize() + range.startIndex, range.length);
                }
            }
        };
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        if (hasAutoUpdates && isDataValid()) {
            //noinspection ConstantConditions
            addListener(adapterData);
        }
    }

    @Override
    public void onDetachedFromRecyclerView(final RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (hasAutoUpdates && isDataValid()) {
            //noinspection ConstantConditions
            removeListener(adapterData);
        }
    }

    @Override
    public int getDataSize() {
        //noinspection ConstantConditions
        return isDataValid() ? adapterData.size() : 0;
    }

    @Override
    public D getItem(int index) {
        //noinspection ConstantConditions
        return isDataValid() ? adapterData.get(index) : null;
    }

    @Nullable
    public OrderedRealmCollection<D> getData() {
        return adapterData;
    }

    @SuppressWarnings("WeakerAccess")
    public void updateData(@Nullable OrderedRealmCollection<D> data) {
        if (hasAutoUpdates) {
            if (isDataValid()) {
                //noinspection ConstantConditions
                removeListener(adapterData);
            }
            if (data != null) {
                addListener(data);
            }
        }

        this.adapterData = data;
        notifyDataSetChanged();
    }

    private void addListener(@NonNull OrderedRealmCollection<D> data) {
        if (data instanceof RealmResults) {
            RealmResults<D> results = (RealmResults<D>) data;
            //noinspection unchecked
            results.addChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList<D> list = (RealmList<D>) data;
            //noinspection unchecked
            list.addChangeListener(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    private void removeListener(@NonNull OrderedRealmCollection<D> data) {
        if (data instanceof RealmResults) {
            RealmResults<D> results = (RealmResults<D>) data;
            //noinspection unchecked
            results.removeChangeListener(listener);
        } else if (data instanceof RealmList) {
            RealmList<D> list = (RealmList<D>) data;
            //noinspection unchecked
            list.removeChangeListener(listener);
        } else {
            throw new IllegalArgumentException("RealmCollection not supported: " + data.getClass());
        }
    }

    private boolean isDataValid() {
        return adapterData != null && adapterData.isValid();
    }

}
