package com.raphaelbussa.baserecyclerview.sample;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by rebus007 on 29/10/17.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(getApplicationContext());
    }
}
