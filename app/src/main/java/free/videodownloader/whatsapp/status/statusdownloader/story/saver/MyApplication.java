package free.videodownloader.whatsapp.status.statusdownloader.story.saver;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;


import com.example.mylibrary.MyAdsApplicationClass;
import com.onesignal.OneSignal;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.AppLangSessionManager;
//import com.facebook.ads.AudienceNetworkAds;
//import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

public class MyApplication extends Application {
    AppLangSessionManager appLangSessionManager;
    private static final String ONESIGNAL_APP_ID = "32384ec8-4e01-4330-98cb-46fa11fd9be0";

    @Override
    public void onCreate() {
        super.onCreate();

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        //open Ads Code
        MyAdsApplicationClass myAdsApplicationClass = new MyAdsApplicationClass();
        myAdsApplicationClass.onCreate(this);
//        AudienceNetworkAds.initialize(this);
//        FirebaseMessaging.getInstance().subscribeToTopic("all");
        appLangSessionManager = new AppLangSessionManager(getApplicationContext());
        setLocale(appLangSessionManager.getLanguage());

    }

    public void setLocale(String lang) {
        if (lang.equals("")){
            lang="en";
        }
        Log.d("Support",lang+"");
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
