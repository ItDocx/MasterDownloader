package com.brainmedia.masterdownloader

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentValues
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.util.SparseArray
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import at.huber.youtubeExtractor.YouTubeUriExtractor
import at.huber.youtubeExtractor.YtFile
import com.brainmedia.masterdownloader.Api.ApiCalls
import com.brainmedia.masterdownloader.Dialogues.AboutusDialoge
import com.brainmedia.masterdownloader.Dialogues.RateUsDialoge
import com.brainmedia.masterdownloader.Utils.NetworkChangeListener
import com.brainmedia.masterdownloader.Web.Links
import com.brainmedia.masterdownloader.databinding.ActivityMainBinding
import com.firebase.client.DataSnapshot
import com.firebase.client.Firebase
import com.firebase.client.FirebaseError
import com.firebase.client.ValueEventListener
import com.google.android.gms.ads.*
import com.google.android.gms.ads.initialization.InitializationStatus
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    //Binding Views and Data
    private lateinit var binding: ActivityMainBinding
    private var onBackPressed = false
    private var dashBoardInter: InterstitialAd? = null
    private var dashBannerCont: RelativeLayout? = null
    private var dashBannerContBottom: RelativeLayout? = null
    //  private val nativeContainer: TemplateView? = null
    private lateinit var toolbar: Toolbar
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var download_url: String
    private val networkchange: NetworkChangeListener = NetworkChangeListener()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        if (intent.getBooleanExtra("exit", false)) {
            finish()
        }
        // Toolbar Setup
        toolbar = findViewById(R.id.customToolbar)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this@MainActivity, binding.drawerLayout,
            toolbar, R.string.open, R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        setToolbar()
        // Find Views
        dashBannerCont = findViewById(R.id.bannerContainer)
        dashBannerContBottom = findViewById(R.id.bottombannerContainer)
        // nativeContainer = findViewById(R.id.nativeAdContainer);


        // nativeContainer = findViewById(R.id.nativeAdContainer);
        isStoragePermissionGranted()

        //Load Native Ad
        //  LoadNativeAd();

        // Initialize Ads

        //Load Native Ad
        //  LoadNativeAd();

        // Initialize Ads
        MobileAds.initialize(
            this
        ) { initializationStatus: InitializationStatus? -> }

        // Implement Bannner Ad

        // Implement Bannner Ad
        TopdashBoardAds()
        BottomBannerAds()

        //Implement Download Interstetial
        //Implement Download Interstetial
        getYtInter()
        getFbDl()
        getInstaDl()
        getTikTokDl()


        // Click Listeners


        // Click Listeners
        binding.btnDownload.setOnClickListener(View.OnClickListener {
            if (binding.etUrl.text.isEmpty()) {
                Toast.makeText(
                    this@MainActivity,
                    "Please Paste Url First for the download",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else if (binding.etUrl.text.toString().contains("youtube.com") || binding.etUrl.text.toString().contains("youtu.be")) {
                if (dashBoardInter != null) {
                    dashBoardInter!!.show(this@MainActivity)
                    dashBoardInter!!.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Toast.makeText(
                                this@MainActivity,
                                "Please wait your Download will begin Shortly",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Implement Nature Interstitial Ad
                            getYtInter()
                            DownloadVideo()
                            super.onAdDismissedFullScreenContent()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            Toast.makeText(
                                this@MainActivity,
                                "Error: " + adError.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
            else if (binding.etUrl.text.toString().contains("tiktok.com")) {
                    val tiktokUrl = binding.etUrl.text.toString()

                if (dashBoardInter != null) {
                    dashBoardInter!!.show(this@MainActivity)
                    dashBoardInter!!.setFullScreenContentCallback(object :
                        FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            Toast.makeText(
                                this@MainActivity,
                                "Please wait your Download will begin Shortly",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Implement TikTok Interstitial Ad
                            getTikTokDl()
                            // Implement Api
                            val tiktokApicall = ApiCalls()
                            tiktokApicall.DownloadTikTokVideos(this@MainActivity, tikUrl = tiktokUrl.toString())
                            Toast.makeText(this@MainActivity, "Download started....!", Toast.LENGTH_SHORT)
                                .show()
                            super.onAdDismissedFullScreenContent()
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            super.onAdFailedToShowFullScreenContent(adError)
                            Toast.makeText(
                                this@MainActivity,
                                "Error: " + adError.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }

                }
            else if (binding.etUrl.text.toString().contains("www.instagram.com")) {

                    if (binding.etUrl.text.toString().contains("reel") || binding.etUrl.text.toString().contains("stories")
                        || binding.etUrl.text.toString().contains("tv") || binding.etUrl.text.toString().contains("/p/")
                    ) {

                        val InstaUrl = binding.etUrl.text.toString()


                        if (dashBoardInter != null) {
                            dashBoardInter!!.show(this@MainActivity)
                            dashBoardInter!!.setFullScreenContentCallback(object :
                                FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Please wait your Download will begin Shortly",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Implement Insta Interstitial Ad
                                    getInstaDl()
                                    // Implement Api
                                    val instaApiCall = ApiCalls()
                                    instaApiCall.DownloadInstaVideos(this@MainActivity, instaUrl = InstaUrl)

                                    super.onAdDismissedFullScreenContent()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    super.onAdFailedToShowFullScreenContent(adError)
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Error: " + adError.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Please Enter Valid Instagram Video Link",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            else if (binding.etUrl.text.toString().contains("www.facebook.com") || binding.etUrl.text.toString().contains("fb.watch") ) {

                    if (binding.etUrl.text.toString().contains("videos")
                        || binding.etUrl.text.toString().contains("reel")
                        || binding.etUrl.text.toString().contains("fb.watch")) {

                        val FbUrl = binding.etUrl.text.toString()

                        if (dashBoardInter != null) {
                            dashBoardInter!!.show(this@MainActivity)
                            dashBoardInter!!.setFullScreenContentCallback(object :
                                FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Please wait your Download will begin Shortly",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    // Implement Fb Interstitial Ad
                                    getFbDl()
                                    // Implement Api
                                    val FbApiCall = ApiCalls()
                                    FbApiCall.DownloadFbVideos(this@MainActivity, Fb_Url = FbUrl)

                                    super.onAdDismissedFullScreenContent()
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    super.onAdFailedToShowFullScreenContent(adError)
                                    Toast.makeText(
                                        this@MainActivity,
                                        "Error: " + adError.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            })
                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Please Enter Valid Facebook Video Link",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            else{
                Toast.makeText(this@MainActivity,"Please Enter Valid Url", Toast.LENGTH_SHORT).show()
            }
        })

        binding.fbIcWeb.setOnClickListener {

            val intent = Intent(this, Links::class.java)
            intent.putExtra("link","https://www.facebook.com/")
            startActivity(intent)

        }

        binding.instaIcWeb.setOnClickListener {

            val intent = Intent(this, Links::class.java)
            intent.putExtra("link","https://www.instagram.com/")
            startActivity(intent)
        }

        binding.tiktokIcWeb.setOnClickListener {

            val intent = Intent(this, Links::class.java)
            intent.putExtra("link","https://www.tiktok.com/")
            startActivity(intent)

        }


    }

    /*  private void LoadNativeAd() {

        AdLoader adLoader = new AdLoader.Builder(MainActivity.this, "ca-app-pub-3940256099942544/2247696110")

                .forNativeAd(nativeAd -> {

                    NativeTemplateStyle nativeStyle = new NativeTemplateStyle.Builder().build();
                    nativeContainer.setStyles(nativeStyle);
                    nativeContainer.setNativeAd(nativeAd);


                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

    }


   */
    /*  private void LoadNativeAd() {

        AdLoader adLoader = new AdLoader.Builder(MainActivity.this, "ca-app-pub-3940256099942544/2247696110")

                .forNativeAd(nativeAd -> {

                    NativeTemplateStyle nativeStyle = new NativeTemplateStyle.Builder().build();
                    nativeContainer.setStyles(nativeStyle);
                    nativeContainer.setNativeAd(nativeAd);


                }).build();
        adLoader.loadAd(new AdRequest.Builder().build());

    }


   */

    private fun isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(ContentValues.TAG, "Permission is granted")
            } else {
                Log.v(ContentValues.TAG, "Permission is revoked")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(ContentValues.TAG, "Permission is granted")
        }
    }

   private fun DownloadVideo() {
        val values: String
        values = binding.etUrl!!.text.toString()
        @SuppressLint("StaticFieldLeak") val youTubeUriExtractor: YouTubeUriExtractor =
            object : YouTubeUriExtractor(this@MainActivity) {
                override fun onUrisAvailable(
                    videoId: String,
                    videoTitle: String,
                    ytFiles: SparseArray<YtFile>
                ) {
                    if (ytFiles != null) {
                        val tag = 22
                        download_url = ytFiles[tag].url
                        if (download_url != null) {
                            val request = DownloadManager.Request(Uri.parse(download_url))
                            request.setTitle(videoTitle).setDescription("Brains Media")
                                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI)
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                .setAllowedOverRoaming(true)
                            request.allowScanningByMediaScanner()
                            request.setDestinationInExternalFilesDir(
                                this@MainActivity,
                                Environment.DIRECTORY_DOWNLOADS, "$videoTitle.mp4"
                            )
                            val downloadManager =
                                getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                            downloadManager.enqueue(request)
                        } else Toast.makeText(
                            this@MainActivity,
                            "Please make sure that the Url is Copied From youtube and the video is no private",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        youTubeUriExtractor.execute(values)
    }

    @SuppressLint("NonConstantResourceId")
     fun setToolbar() {
        binding.navMain!!.setNavigationItemSelectedListener { item: MenuItem ->
            val id = item.itemId
            when (id) {
                R.id.menu_home -> binding.drawerLayout!!.closeDrawer(GravityCompat.START)

                R.id.menu_fb->{
                    val intent = Intent(this, Links::class.java)
                    intent.putExtra("link","https://www.facebook.com/")
                    startActivity(intent)
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }

                R.id.menu_tiktok -> {
                    val intent = Intent(this, Links::class.java)
                    intent.putExtra("link","https://www.tiktok.com/")
                    startActivity(intent)
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_insta -> {
                    val intent = Intent(this, Links::class.java)
                    intent.putExtra("link","https://www.instagram.com/")
                    startActivity(intent)
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_feedback -> {
                    settingRateUsDialoge()
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_about_us -> {
                    setAboutUsDialoge()
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
                R.id.menu_exit -> {
                    Toast.makeText(this@MainActivity, "Exiting Application", Toast.LENGTH_LONG)
                        .show()
                    finish()
                    binding.drawerLayout!!.closeDrawer(GravityCompat.START)
                }
            }
            true
        }
    }

     fun settingRateUsDialoge() {
        val rate = RateUsDialoge(this@MainActivity)
        rate.getWindow()?.setBackgroundDrawable(
            ColorDrawable(
                resources
                    .getColor(android.R.color.white)
            )
        )
        rate.setCancelable(false)
        rate.show()
    }

    fun setAboutUsDialoge() {
        val dashBoardDialog = AboutusDialoge(this@MainActivity)
        dashBoardDialog.setCancelable(false)
        dashBoardDialog.show()
    }


    // Get Download Interstetial
    private fun getYtInter() {
        Firebase.setAndroidContext(this@MainActivity)
        val interFirebase =
            Firebase("https://master-downloader-648f1-default-rtdb.firebaseio.com/ytInter")
        interFirebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val ytDlInter = dataSnapshot.getValue(String::class.java)
                setYtInter(ytDLInter = ytDlInter)
            }

            override fun onCancelled(firebaseError: FirebaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: " + firebaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun getTikTokDl() {
        Firebase.setAndroidContext(this@MainActivity)
        val interFirebase =
            Firebase("https://master-downloader-648f1-default-rtdb.firebaseio.com/tikTokInter")
        interFirebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tikTOKDlInter = dataSnapshot.getValue(String::class.java)
                setTikTokDl(tikTokDlInter = tikTOKDlInter)
            }

            override fun onCancelled(firebaseError: FirebaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: " + firebaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun getFbDl() {
        Firebase.setAndroidContext(this@MainActivity)
        val interFirebase =
            Firebase("https://master-downloader-648f1-default-rtdb.firebaseio.com/fbInter")
        interFirebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val FbDlInter = dataSnapshot.getValue(String::class.java)
                setFbDl(fbDlId = FbDlInter)
            }

            override fun onCancelled(firebaseError: FirebaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: " + firebaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun getInstaDl() {
        Firebase.setAndroidContext(this@MainActivity)
        val interFirebase =
            Firebase("https://master-downloader-648f1-default-rtdb.firebaseio.com/instaInter")
        interFirebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val instADlInter = dataSnapshot.getValue(String::class.java)
                setInstaDl(instaDlInter = instADlInter)
            }

            override fun onCancelled(firebaseError: FirebaseError) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: " + firebaseError.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    // Set Download Interstetial Ad
    private fun setYtInter(ytDLInter: String) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            ytDLInter,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    dashBoardInter = null
                    Toast.makeText(
                        this@MainActivity,
                        "Error: " + loadAdError.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    dashBoardInter = interstitialAd
                }
            })
    }
    private fun setTikTokDl(tikTokDlInter:String){

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            tikTokDlInter,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    dashBoardInter = null
                    Toast.makeText(
                        this@MainActivity,
                        "Error: " + loadAdError.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    dashBoardInter = interstitialAd
                }
            })


    }
    private fun setInstaDl(instaDlInter:String){


        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            instaDlInter,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    dashBoardInter = null
                    Toast.makeText(
                        this@MainActivity,
                        "Error: " + loadAdError.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    dashBoardInter = interstitialAd
                }
            })

    }
    private fun setFbDl(fbDlId:String){

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            fbDlId,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    dashBoardInter = null
                    Toast.makeText(
                        this@MainActivity,
                        "Error: " + loadAdError.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    dashBoardInter = interstitialAd
                }
            })
    }

    // Set Banners

    private fun BottomBannerAds(){


        Firebase.setAndroidContext(this)
        val firebase =
            Firebase("https://master-downloader-648f1-default-rtdb.firebaseio.com/bottomBanner")
        firebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(String::class.java)
                val bannerAd = AdView(this@MainActivity)
                bannerAd.adUnitId = data
                bannerAd.setAdSize(AdSize.SMART_BANNER)
                dashBannerContBottom!!.addView(bannerAd)
                val adRequest = AdRequest.Builder().build()
                bannerAd.loadAd(adRequest)
            }
                override fun onCancelled(firebaseError: FirebaseError) {}
            })

    }
    private fun TopdashBoardAds() {
        Firebase.setAndroidContext(this)
        val firebase =
            Firebase("https://master-downloader-648f1-default-rtdb.firebaseio.com/topBanner")
        firebase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.getValue(String::class.java)
                val bannerAd = AdView(this@MainActivity)
                bannerAd.adUnitId = data
                bannerAd.setAdSize(AdSize.SMART_BANNER)
                dashBannerCont!!.addView(bannerAd)
                val adRequest = AdRequest.Builder().build()
                bannerAd.loadAd(adRequest)
            }

            override fun onCancelled(firebaseError: FirebaseError) {}
        })
    }


    // Implement Double Back Pressed
    override fun onBackPressed() {
        if (onBackPressed) {
            super.onBackPressed()
        } else {
            onBackPressed = true
            Toast.makeText(this, "Press Again to exit", Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ onBackPressed = false }, 3000)
        }
    }

    override fun onStart() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkchange, intentFilter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkchange)
        super.onStop()
    }

}