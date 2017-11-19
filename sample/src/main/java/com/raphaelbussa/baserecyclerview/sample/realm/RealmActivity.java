package com.raphaelbussa.baserecyclerview.sample.realm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.raphaelbussa.baserecyclerview.BaseAdapter;
import com.raphaelbussa.baserecyclerview.BaseRecyclerView;
import com.raphaelbussa.baserecyclerview.realm.BaseRealmAdapter;
import com.raphaelbussa.baserecyclerview.sample.MainActivity;
import com.raphaelbussa.baserecyclerview.sample.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by rebus007 on 29/10/17.
 */

public class RealmActivity extends AppCompatActivity {

    private BaseRealmAdapter<ItemModel, RealmViewHolder> mainAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout root = findViewById(R.id.root);


        BaseRecyclerView baseRecyclerView = findViewById(R.id.list);

        Realm realm = Realm.getDefaultInstance();
        RealmResults<ItemModel> query = realm.where(ItemModel.class).findAllSorted("id");
        mainAdapter = new BaseRealmAdapter<>(RealmViewHolder.class,
                R.layout.row_realm_test,
                query,
                true,
                true);
        mainAdapter.setOnClickListener(new BaseAdapter.OnClickListener<ItemModel>() {
            @Override
            public void onClick(View view, ItemModel item, int position) {
                Toast.makeText(RealmActivity.this, item.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        mainAdapter.setOnLongClickListener(new BaseAdapter.OnLongClickListener<ItemModel>() {
            @Override
            public boolean onLongClick(View view, ItemModel item, int position) {
                Realm realm1 = Realm.getDefaultInstance();
                realm1.beginTransaction();
                item.deleteFromRealm();
                realm1.commitTransaction();
                return true;
            }
        });

        mainAdapter.addHeader(root, R.layout.header_test);
        mainAdapter.addHeader(root, R.layout.header_test);

        mainAdapter.addFooter(root, R.layout.footer_test);
        mainAdapter.addFooter(root, R.layout.footer_test);


        baseRecyclerView.setAdapter(mainAdapter);

        realm.beginTransaction();

        for (int i = 0; i < 20; i++) {
            ItemModel model = new ItemModel();
            model.setId(i);
            String text = "test row ";
            for (int i1 = 0; i1 < i; i1++) {
                text += text;
            }
            model.setText("Realm row " + text);
            realm.copyToRealmOrUpdate(model);
        }

        realm.commitTransaction();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Realm realm;
        ItemModel model;
        ArrayList<MainActivity.Bean> beanArrayList;
        switch (item.getItemId()) {
            case R.id.remove:
                realm = Realm.getDefaultInstance();
                model = realm.where(ItemModel.class).equalTo("id", 1).findFirst();
                if (model != null) {
                    realm.beginTransaction();
                    model.deleteFromRealm();
                    realm.commitTransaction();
                }
                return true;
            case R.id.add_pos:
                realm = Realm.getDefaultInstance();
                model = new ItemModel();
                model.setId(100);
                model.setText("Realm row " + 100);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(model);
                realm.commitTransaction();
                return true;
            case R.id.add:
                realm = Realm.getDefaultInstance();
                model = new ItemModel();
                model.setId(101);
                model.setText("Realm row " + 101);
                realm.beginTransaction();
                realm.copyToRealmOrUpdate(model);
                realm.commitTransaction();
                return true;
            case R.id.add_4:
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < 4; i++) {
                    model = new ItemModel();
                    model.setId(i);
                    model.setText("Realm row 20" + i);
                    realm.copyToRealmOrUpdate(model);
                }
                realm.commitTransaction();
                return true;
            case R.id.add_4_pos:
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < 4; i++) {
                    model = new ItemModel();
                    model.setId(i);
                    model.setText("Realm row 40" + i);
                    realm.copyToRealmOrUpdate(model);
                }
                realm.commitTransaction();
                return true;
            case R.id.clear:
                realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.delete(ItemModel.class);
                realm.commitTransaction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
