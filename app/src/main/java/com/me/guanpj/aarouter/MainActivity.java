package com.me.guanpj.aarouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.me.guanpj.aarouter.annotations.Destination;

@Destination(url = "router://page-home", description = "main page")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}