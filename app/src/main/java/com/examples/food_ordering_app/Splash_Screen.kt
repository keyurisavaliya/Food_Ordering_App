package com.examples.food_ordering_app

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper


class Splash_Screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        startHeavyTask()
//        Handler(Looper.getMainLooper()).postDelayed({
//            val  intent = Intent(this,LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        },2000)
    }

    private fun startHeavyTask() {

        LongOperation().execute()
    }

    private open inner class LongOperation : AsyncTask<String?,Void?,String?>(){
        override fun doInBackground(vararg p0: String?): String? {
            for(i in 0..3){
                try {
                    Thread.sleep(1000)
                }catch (e:Exception){
                    Thread.interrupted()
                }
            }
            return "result"
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val intent = Intent(this@Splash_Screen,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}