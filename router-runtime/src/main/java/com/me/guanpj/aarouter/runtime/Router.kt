package com.me.guanpj.aarouter.runtime

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log

object Router {
    private const val GENERATED_MAPPING_CLASS = "com.me.guanpj.aarouter.mapping.generated.RouterMapping"
    private val map: HashMap<String, String> = HashMap()

    fun init() {
        try {
            val mapping = Class.forName(GENERATED_MAPPING_CLASS).getMethod("get").invoke(null) as Map<String, String>
            if (mapping.isNotEmpty()) {
                mapping.forEach {
                    Log.e("gpj", "mapping item : ${it.key} -> ${it.value}")
                }
                map.putAll(mapping)
            }
        } catch (e: Exception) {
        }
    }

    fun go(context: Context, url: String) {

        if (map.isEmpty()) {
            Log.e("gpj", "RouterMapping size is empty!")
            return
        }

        val uri = Uri.parse(url)
        val scheme = uri.scheme
        val host = uri.host
        val path = uri.path

        var targetActivityClass = ""

        map.onEach {
            val curUri = Uri.parse(it.key)
            val curScheme = curUri.scheme
            val curHost = curUri.host
            val curPath = curUri.path

            if (curScheme == scheme && curHost == host && curPath == path) {
                targetActivityClass = it.value
            }
        }

        if (targetActivityClass == "") {
            return
        }

        val bundle = Bundle()
        val query = uri.query
        query?.let {
            if (it.length >= 3) {
                val args = it.split("&")
                args.onEach { arg ->
                    val splits = arg.split("=")
                    if (splits.isNotEmpty() && splits.size == 2) {
                        bundle.putString(splits[0], splits[1])
                    }
                }
            }
        }

        try {
            val activity = Class.forName(targetActivityClass)
            val intent = Intent(context, activity)
            intent.putExtras(bundle)
            context.startActivity(intent)
        } catch (e : Throwable) {
            Log.e("gpj", "go: error while start activity: $targetActivityClass, e = $e")
        }
    }
}