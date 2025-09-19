package com.example.atvcronometro

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

open class DebugActivity: AppCompatActivity() {

    companion object{
        val TAG = "si-urutai"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, getClassName() + ".onCreate()chamado:")
    }
    override fun onStart() {
        super.onStart()
        Log.i(DebugActivity.TAG, getClassName() + ".onStart() chamado.")
    }
    override fun onRestart() {
        super.onRestart()
        Log.i(DebugActivity.TAG, getClassName() + ".onRestart() chamado.")
    }
    override fun onResume() {
        super.onResume()
        Log.i(DebugActivity.TAG, getClassName() + ".onResume() chamado.")
    }

    override fun onPause() {
        super.onPause()
        Log.i(DebugActivity.TAG, getClassName() + ".onPause() chamado.")
    }
    protected override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState!!)
        Log.i(DebugActivity.TAG, getClassName() + ".onSaveInstanceState() chamado.")
    }
    override fun onStop() {
        super.onStop()
        Log.i(DebugActivity.TAG, getClassName() + ".onStop() chamado.")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.i(DebugActivity.TAG, getClassName() + ".onDestroy() chamado.")
    }

    private fun getClassName(): String {
        val s = javaClass.name
        return s.substring(s.lastIndexOf("."))

    }
}