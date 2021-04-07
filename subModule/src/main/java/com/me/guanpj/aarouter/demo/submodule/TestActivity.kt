package com.me.guanpj.aarouter.demo.submodule

import androidx.appcompat.app.AppCompatActivity
import com.me.guanpj.aarouter.annotations.Destination

@Destination(url = "router://sub-test", description = "test page")
class TestActivity: AppCompatActivity() {
}