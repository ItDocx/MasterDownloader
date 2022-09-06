package com.brainmedia.masterdownloader

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.MySplashScreen)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        //getNativeAd()

        val handler = Handler()
        handler.postDelayed({ // Get Native Ad
            //getNativeAd()
            startActivity(Intent(this@Splash, MainActivity::class.java))
            finish()
        }, 3000)


    }

  /*  private fun getNativeAd() {
        val data = FirebaseDatabase.getInstance()
        val ref = data.getReference("native")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Ads.nativeId = snapshot.child("native").getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

   */

}