package com.raphaelbussa.baserecyclerview.sample;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.raphaelbussa.baserecyclerview.BaseAdapter;
import com.raphaelbussa.baserecyclerview.BaseRecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private BaseAdapter<Bean, MainViewHolder> mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BaseRecyclerView baseRecyclerView = findViewById(R.id.list);
        RelativeLayout root = findViewById(R.id.root);

        mainAdapter = new BaseAdapter<>(MainViewHolder.class, R.layout.row_test);

        mainAdapter.addAction(R.drawable.ic_delete_white, ContextCompat.getColor(this, R.color.colorAccent), ItemTouchHelper.LEFT);
        mainAdapter.addAction(R.drawable.ic_edit_white, ContextCompat.getColor(this, R.color.colorPrimaryDark), ItemTouchHelper.RIGHT);

        baseRecyclerView.setAdapter(mainAdapter);
        for (int i = 0; i < 2; i++) {
            mainAdapter.add(new Bean(i + 1));
        }
        mainAdapter.setOnClickListener(new BaseAdapter.OnClickListener<Bean>() {
            @Override
            public void onClick(View view, Bean item, int position) {
                Toast.makeText(MainActivity.this, "text: " + item.getText() + " pos: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        mainAdapter.addHeader(root, R.layout.header_test);
        mainAdapter.addHeader(root, R.layout.header_test);

        mainAdapter.addFooter(root, R.layout.footer_test);
        mainAdapter.addFooter(root, R.layout.footer_test);

        mainAdapter.setOnLongClickListener(new BaseAdapter.OnLongClickListener<Bean>() {
            @Override
            public boolean onLongClick(View view, Bean item, int position) {
                mainAdapter.remove(item);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<Bean> beanArrayList;
        switch (item.getItemId()) {
            case R.id.remove:
                mainAdapter.remove(4);
                return true;
            case R.id.add_pos:
                mainAdapter.add(new Bean(4), 4);
                return true;
            case R.id.add:
                mainAdapter.add(new Bean(100));
                return true;
            case R.id.add_4:
                beanArrayList = new ArrayList<>();
                beanArrayList.add(new Bean(200));
                beanArrayList.add(new Bean(201));
                beanArrayList.add(new Bean(202));
                beanArrayList.add(new Bean(203));
                mainAdapter.addAll(beanArrayList);
                return true;
            case R.id.add_4_pos:
                beanArrayList = new ArrayList<>();
                beanArrayList.add(new Bean(400));
                beanArrayList.add(new Bean(401));
                beanArrayList.add(new Bean(402));
                beanArrayList.add(new Bean(403));
                mainAdapter.addAll(beanArrayList, 4);
                return true;
            case R.id.clear:
                mainAdapter.clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class Bean {

        private int image;
        private String text;

        public Bean(int pos) {
            this.image = R.mipmap.ic_launcher;
            this.text = "Row number " + pos;
        }

        public int getImage() {
            return image;
        }

        public void setImage(int image) {
            this.image = image;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

}
