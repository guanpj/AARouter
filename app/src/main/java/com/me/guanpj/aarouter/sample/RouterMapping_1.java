package com.me.guanpj.aarouter.sample;

import java.util.HashMap;
import java.util.Map;

public class RouterMapping_1 {

    public static Map<String, String> get() {

        Map<String, String> mapping = new HashMap<>();

        mapping.put("router://page-kotlin", "com.imooc.router.demo.KtMainActivity");
        mapping.put("router://page-home", "com.imooc.router.demo.MainActivity");
        return mapping;
    }
}
