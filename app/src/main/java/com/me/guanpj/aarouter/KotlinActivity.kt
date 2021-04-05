package com.me.guanpj.aarouter

import androidx.appcompat.app.AppCompatActivity
import com.me.guanpj.aarouter.annotations.Destination

@Destination(url = "router://page-kotlin", description = "kotlin page")
class KotlinActivity: AppCompatActivity() {
}