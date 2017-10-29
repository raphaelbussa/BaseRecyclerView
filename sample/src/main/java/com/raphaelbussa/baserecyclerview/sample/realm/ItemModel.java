package com.raphaelbussa.baserecyclerview.sample.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by rebus007 on 29/10/17.
 */

public class ItemModel extends RealmObject {

    @PrimaryKey
    private int id;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
