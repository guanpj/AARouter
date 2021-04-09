package com.me.guanpj.aarouter;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.me.guanpj.aarouter.annotations.Destination;
import com.me.guanpj.aarouter.runtime.Router;

@Destination(url = "router://page-home", description = "main page")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_go).setOnClickListener(v -> {
            Router.INSTANCE.go(v.getContext(), "router://sub-test?name=guanpj&message=hello");
        });
    }
}

