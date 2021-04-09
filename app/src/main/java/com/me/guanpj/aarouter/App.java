package com.me.guanpj.aarouter;

import android.app.Application;

import com.me.guanpj.aarouter.runtime.Router;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Router.INSTANCE.init();
    }
}
