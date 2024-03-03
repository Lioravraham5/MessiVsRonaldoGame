package com.example.messivsronaldo;

import android.app.Application;

import com.example.messivsronaldo.Utilities.SharedPreferencesManager;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
    }
}
