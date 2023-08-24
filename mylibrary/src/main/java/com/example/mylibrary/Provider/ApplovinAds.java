package com.example.mylibrary.Provider;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.example.mylibrary.AdsConstant;
import com.example.mylibrary.BaseClass;
import com.example.mylibrary.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ApplovinAds extends AdsFormat {

    //Defaults
    public static ApplovinAds instance;
    public static Activity activity;

    //Applovin Ids
    private static String fullScreenAdId = "";//YOUR_AD_UNIT_ID
    private static String NativeId = "";//YOUR_AD_UNIT_ID
    private static String Banner_ID = "";//YOUR_AD_UNIT_ID
    private static String Reward_ID = "";//YOUR_AD_UNIT_ID

    public static String TAG = "AMS@ApplovinAds";//BaseClass.class.getSimpleName();
    //Applovin loaded tags


    //ApplovinAds ad variables
    MaxAdView adView;
    private MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    private MaxNativeAdLoader nativeAdLoader;
    private MaxAd nativeAd;
    MaxNativeAdView maxNativeAdView;
    private MaxRewardedAd rewardedAd;

    public ApplovinAds(Activity activity) {
        this.activity = activity;
    }

    public static ApplovinAds getInstance(Activity activity1) {
        activity = activity1;
        if (instance == null) {
            instance = new ApplovinAds(activity1);
        }
        return instance;
    }


    @Override
    public void preloadAds(JSONObject jsonObject) {
        Log.i(TAG, "preloadAds: Method Call");
        setAdsId(jsonObject);
        AppLovinSdk.getInstance(activity).setMediationProvider("max");
        AppLovinSdk.initializeSdk(activity, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration) {
                // AppLovin SDK is initialized, start loading ads
                Log.i(TAG, "preloadAds_onSdkInitialized: " + configuration);
                preloadBannerAds();
                preloadNativeAds();
                preloadInterstitialAd();
                preloadRewardAd();
            }
        });
    }

    private void preloadRewardAd() {
        rewardedAd = MaxRewardedAd.getInstance(Reward_ID, activity);/*"1cd4bea52caa721d"*/
        rewardedAd.setListener(new MaxRewardedAdListener() {
            @Override
            public void onUserRewarded(MaxAd maxAd, MaxReward maxReward) {
             // Rewarded ad was displayed and user should receive the reward
                BaseClass.rewardCallBack(true);
            }

            @Override
            public void onRewardedVideoStarted(MaxAd maxAd) {
// deprecated
            }

            @Override
            public void onRewardedVideoCompleted(MaxAd maxAd) {
// deprecated
            }

            @Override
            public void onAdLoaded(MaxAd maxAd) {
// Rewarded ad is ready to be shown. rewardedAd.isReady() will now return 'true'
                setRewardLoaded(true);
                // Reset retry attempt
                retryAttempt = 0;
            }

            @Override
            public void onAdDisplayed(MaxAd maxAd) {

            }

            @Override
            public void onAdHidden(MaxAd maxAd) {
                // rewarded ad is hidden. Pre-load the next ad
                rewardedAd.loadAd();
            }

            @Override
            public void onAdClicked(MaxAd maxAd) {

            }

            @Override
            public void onAdLoadFailed(String s, MaxError maxError) {
                // Rewarded ad failed to load
                // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)

                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis( (long) Math.pow( 2, Math.min( 6, retryAttempt ) ) );

                new Handler().postDelayed( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        rewardedAd.loadAd();
                    }
                }, delayMillis );
                BaseClass.rewardCallBack(false);
            }

            @Override
            public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                // Rewarded ad failed to display. We recommend loading the next ad

                rewardedAd.loadAd();
                BaseClass.rewardCallBack(false);
            }
        });
        rewardedAd.loadAd();
    }

    private void preloadBannerAds() {
        Log.i(TAG, "preloadBannerAds Method Call: ");
        if (isBannerLoaded()) {
            Log.i(TAG, "preloadBannerAds Already Preloaded :return");
            return;
        }
        adView = new MaxAdView(Banner_ID, activity);
        adView.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {
                Log.i(TAG, "preloadBannerAds_onAdExpanded: " + ad.getAdUnitId());
            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
                Log.i(TAG, "preloadBannerAds_onAdCollapsed: " + ad.getAdUnitId());
            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.i(TAG, "preloadBannerAds_onAdLoaded: " + ad.getAdUnitId());
                setBannerLoaded(true);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Log.i(TAG, "preloadBannerAds_onAdDisplayed: " + ad.getAdUnitId());
            }

            @Override
            public void onAdHidden(MaxAd ad) {
                Log.i(TAG, "preloadBannerAds_onAdHidden: " + ad.getAdUnitId());
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.i(TAG, "preloadBannerAds_onAdClicked: " + ad.getAdUnitId());
            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.i(TAG, "preloadBannerAds_onAdLoadFailed: " + error.getMessage());
                setBannerLoaded(false);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.i(TAG, "preloadBannerAds_onAdDisplayFailed: " + error.getMessage());
                setBannerLoaded(false);
            }
        });
//        int width = ViewGroup.LayoutParams.MATCH_PARENT;
//        int heightPx = ViewGroup.LayoutParams.WRAP_CONTENT;
//        int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.banner_height);
//        adView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
        adView.loadAd();
    }

    private void preloadInterstitialAd() {
        Log.i(TAG, "preloadInterstitialAd Method Call: ");
        if (isInterstitialLoaded()) {
            Log.i(TAG, "preloadInterstitialAd Already Preloaded :return");
            return;
        }
        interstitialAd = new MaxInterstitialAd(fullScreenAdId, activity);
        interstitialAd.setListener(new MaxAdViewAdListener() {
            @Override
            public void onAdExpanded(MaxAd ad) {
                Log.i(TAG, "preloadInterstitialAd_onAdExpanded: " + ad.getAdUnitId());
            }

            @Override
            public void onAdCollapsed(MaxAd ad) {
                Log.i(TAG, "preloadInterstitialAd_onAdCollapsed: " + ad.getAdUnitId());
                AfterDismissInterstitial();
                preloadInterstitialAd();
            }

            @Override
            public void onAdLoaded(MaxAd ad) {
                Log.i(TAG, "preloadInterstitialAd_onAdLoaded: " + ad.getAdUnitId());
                retryAttempt = 0;
                setInterstitialLoaded(true);
            }

            @Override
            public void onAdDisplayed(MaxAd ad) {
                Log.i(TAG, "preloadInterstitialAd_onAdDisplayed: " + ad.getAdUnitId());

            }

            @Override
            public void onAdHidden(MaxAd ad) {
                Log.i(TAG, "preloadInterstitialAd_onAdHidden: " + ad.getAdUnitId());
                // Interstitial ad is hidden. Pre-load the next ad
                AfterDismissInterstitial();
                preloadInterstitialAd();
            }

            @Override
            public void onAdClicked(MaxAd ad) {
                Log.i(TAG, "preloadInterstitialAd_onAdClicked: " + ad.getAdUnitId());

            }

            @Override
            public void onAdLoadFailed(String adUnitId, MaxError error) {
                Log.i(TAG, "preloadInterstitialAd_onAdLoadFailed: " + error.getMessage());
                setInterstitialLoaded(false);
                retryAttempt++;
                long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interstitialAd.loadAd();
                    }
                }, delayMillis);
            }

            @Override
            public void onAdDisplayFailed(MaxAd ad, MaxError error) {
                Log.i(TAG, "preloadInterstitialAd_onAdDisplayFailed: " + error.getMessage());
                // Interstitial ad failed to display. AppLovin recommends that you load the next ad.
                interstitialAd.loadAd();
            }
        });

        // Load the first ad
        interstitialAd.loadAd();
    }

    private void preloadNativeAds() {
        Log.i(TAG, "preloadNativeAds Method Call: ");
        if (isNativeLoaded()) {
            Log.i(TAG, "preloadNativeAds Already Preloaded :return");
            return;
        }
        nativeAdLoader = new MaxNativeAdLoader(NativeId, activity);
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, final MaxAd ad) {
                // Clean up any pre-existing native ad to prevent memory leaks.
                if (nativeAd != null) {
                    nativeAdLoader.destroy(nativeAd);
                }

                // Save ad for cleanup.
                nativeAd = ad;
                maxNativeAdView = nativeAdView;
                setNativeLoaded(true);

            }

            @Override
            public void onNativeAdLoadFailed(final String adUnitId, final MaxError error) {
                // We recommend retrying with exponentially higher delays up to a maximum delay
                setNativeLoaded(false);
            }

            @Override
            public void onNativeAdClicked(final MaxAd ad) {
                // Optional click callback
            }
        });

        nativeAdLoader.loadAd();
    }


    @Override
    public void setAdsId(JSONObject jsonObject) {
        try {
            JSONObject googleJson = jsonObject.getJSONObject(AdsConstant.ApplovinADS);
            Banner_ID = googleJson.getString(AdsConstant.BannerAD_ID);
            fullScreenAdId = googleJson.getString(AdsConstant.FullScreen_ID);
            NativeId = googleJson.getString(AdsConstant.Native_ID);
            Reward_ID = googleJson.getString(AdsConstant.RewardAd_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showInterstitialAds() {
        Log.v(TAG, "showInterstitialAds");
        if (interstitialAd != null) {
            if (interstitialAd.isReady()) {
                interstitialAd.showAd();
                setInterstitialLoaded(false);
            }
        }
    }

    @Override
    public void showBannerAds(LinearLayout adLayout) {
        Log.v(TAG, "showBannerAds");
        if (adLayout != null) {
            adLayout.removeAllViews();
            if (adView != null) {
                adLayout.setGravity(Gravity.BOTTOM);
                adLayout.addView(adView);
                setBannerLoaded(false);
                preloadBannerAds();
            }
        }
    }

    @Override
    public void showNative(LinearLayout layout, ImageView img) {
        Log.v(TAG, "showNative");
        if (layout != null) {
            layout.removeAllViews();
            if (nativeAd != null) {
                layout.removeAllViews();
                layout.addView(maxNativeAdView);
                setNativeLoaded(false);
                if (img != null) {
                    img.setVisibility(View.GONE);
                }
                preloadNativeAds();
            }
        }
    }

    @Override
    public void showRewardAds() {
        if ( rewardedAd.isReady()) {
            rewardedAd.showAd();
        }else {
            BaseClass.rewardCallBack(false);
        }
    }
}
