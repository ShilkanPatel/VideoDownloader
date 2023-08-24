package com.example.mylibrary;

import static com.example.mylibrary.Provider.GoogleAds.isGoogleBannerLoaded;
import static com.example.mylibrary.Provider.GoogleAds.isGoogleInterstitialLoaded;
import static com.example.mylibrary.Provider.GoogleAds.isGoogleNativeLoaded;
import static com.example.mylibrary.Provider.MopUpAds.isMoPubBannerLoaded;
import static com.example.mylibrary.Provider.MopUpAds.isMoPubInterstitialLoaded;
import static com.example.mylibrary.Provider.MopUpAds.isMoPubNativeLoaded;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.Provider.ApplovinAds;
import com.example.mylibrary.Provider.FaceBookAds;
import com.example.mylibrary.Provider.GoogleAds;
import com.example.mylibrary.Provider.IronSourceAds;
import com.example.mylibrary.Provider.MopUpAds;
import com.example.mylibrary.Provider.UnityAds;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseClass extends AppCompatActivity {

    public static boolean SHOW_AD = true;
    public static String AD_TIME = "0";
    public static String AD_TAP_COUNT = "0";
    public static int AD_Reward_COUNT = 0;
    public static String FirstAd = "";
    public static String SecondAd = "";


    public static boolean showFullAd = true;
    public static int activityCount = 0;
    public static Activity activity;
    public static BaseClass instance;

    static com.example.mylibrary.InterstitialDismissCallback interstitialDismissCallback;
    static com.example.mylibrary.RewardCallback rewardCallback;

    public BaseClass(Activity activity1) {
        activity = activity1;
    }

    public static BaseClass getInstance(Activity activity) {
        if (instance == null) {
            instance = new BaseClass(activity);
        }
        return instance;
    }

    public void initAds(JSONObject jsonObject) {
        setAdsSetting(jsonObject);
        preLoadAllAds(FirstAd, jsonObject);
        preLoadAllAds(SecondAd, jsonObject);
    }

    private void setAdsSetting(JSONObject jsonObject) {
        try {
            SHOW_AD = jsonObject.getBoolean("AdShow");
            AD_TIME = jsonObject.getString("ShowAd_After_Time_In_Sec");
            AD_TAP_COUNT = jsonObject.getString("ShowAd_After_Taps");
            FirstAd = jsonObject.getString("FirstAd");
            SecondAd = jsonObject.getString("SecondAd");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void preLoadAllAds(String AdType, JSONObject jsonObject) {
        switch (AdType) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                GoogleAds.getInstance(activity).preloadGoogleAds(jsonObject);
                break;
            case com.example.mylibrary.AdsConstant.MopUpADS:
                MopUpAds.getInstance(activity).preloadMopUpAds(jsonObject);
                break;
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                FaceBookAds.getInstance(activity).preloadAds(jsonObject);
                break;
            case com.example.mylibrary.AdsConstant.UnityADS:
                UnityAds.getInstance(activity).preloadAds(jsonObject);
                break;
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                ApplovinAds.getInstance(activity).preloadAds(jsonObject);
                break;
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                IronSourceAds.getInstance(activity).preloadAds(jsonObject);
                break;
        }
    }

    public static void interstitialCallBack() {
        if (interstitialDismissCallback != null) {
            interstitialDismissCallback.callbackCall();
            interstitialDismissCallback = null;
        }
    }

    public static void rewardCallBack(boolean isRewardEran) {
        if (rewardCallback != null) {
            rewardCallback.callbackCall(isRewardEran);
            rewardCallback = null;
        }
    }

    public static void startAdHandler() {
//        showFullAd = true;
        int FULL_HANDLER_TIME = 1000 * Integer.parseInt(AD_TIME);
        new Handler(Looper.getMainLooper()).postDelayed(() -> showFullAd = true, FULL_HANDLER_TIME);
    }

    public void showBannerAd(LinearLayout layout) {
        layout.addView(getBannerView(activity));
        layout.setVisibility(View.VISIBLE);
        if (SHOW_AD) {
            showFirstBanner(layout);
        } else {
            layout.setVisibility(View.GONE);
        }
    }

    private void showFirstBanner(LinearLayout layout) {
        switch (FirstAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleBannerLoaded) {
                    GoogleAds.getInstance(activity).showGoogleBanner(layout);
                } else {
                    showSecondBanner(layout);
                }
                break;
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubBannerLoaded) {
                    MopUpAds.getInstance(activity).showMopUpBanner(layout);
                } else {
                    showSecondBanner(layout);
                }
                break;
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isBannerLoaded()) {
                    FaceBookAds.getInstance(activity).showBannerAds(layout);
                } else {
                    showSecondBanner(layout);
                }
                break;
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isBannerLoaded()) {
                    UnityAds.getInstance(activity).showBannerAds(layout);
                } else {
                    showSecondBanner(layout);
                }
                break;
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isBannerLoaded()) {
                    ApplovinAds.getInstance(activity).showBannerAds(layout);
                } else {
                    showSecondBanner(layout);
                }
                break;
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isBannerLoaded()) {
                    IronSourceAds.getInstance(activity).showBannerAds(layout);
                } else {
                    showSecondBanner(layout);
                }
                break;
        }
    }

    private void showSecondBanner(LinearLayout layout) {
        switch (SecondAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleBannerLoaded) {
                    GoogleAds.getInstance(activity).showGoogleBanner(layout);
                } else {
//                    layout.setVisibility(View.GONE);
                }
                break;
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubBannerLoaded) {
                    MopUpAds.getInstance(activity).showMopUpBanner(layout);
                } else {
//                    layout.setVisibility(View.GONE);
                }
                break;
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isBannerLoaded()) {
                    FaceBookAds.getInstance(activity).showBannerAds(layout);
                } else {
//                    layout.setVisibility(View.GONE);
                }
                break;
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isBannerLoaded()) {
                    UnityAds.getInstance(activity).showBannerAds(layout);
                } else {
//                    layout.setVisibility(View.GONE);
                }
                break;
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isBannerLoaded()) {
                    ApplovinAds.getInstance(activity).showBannerAds(layout);
                } else {
//                    layout.setVisibility(View.GONE);
                }
                break;
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isBannerLoaded()) {
                    IronSourceAds.getInstance(activity).showBannerAds(layout);
                } else {
//                    layout.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void showInterstitialAds(com.example.mylibrary.InterstitialDismissCallback callback) {
        BaseClass.interstitialDismissCallback = callback;

        if (SHOW_AD && showFullAd) {
            switch (AD_TAP_COUNT) {
                case "0":
                    NotshowAd();
                    break;
                case "1":
                    showFirstInterstitial();
                    break;
                case "2":
                    if (activityCount % 2 == 0) {
                        showFirstInterstitial();
                    } else {
                        NotshowAd();
                    }
                    activityCount++;
                    break;
                case "3":
                    if (activityCount % 3 == 0) {
                        showFirstInterstitial();
                    } else {
                        NotshowAd();
                    }
                    activityCount++;
                    break;
                default:
                    NotshowAd();
                    break;

            }
        } else {
            NotshowAd();
        }
    }

    public void showRewardAds(RewardCallback rewardCallback) {
        BaseClass.rewardCallback = rewardCallback;
        showFirstReward();
    }

    private void NotshowAd() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            interstitialCallBack();
        }, 100 /*1000*/);
    }

    private void NotshowRewardAd() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            rewardCallBack(false);
        }, 100 /*1000*/);
    }

    private void showFirstInterstitial() {
        switch (FirstAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleInterstitialLoaded) {
                    GoogleAds.getInstance(activity).showGoogleInterstitial();
                } else {
                    showSecondInterstitial();
                }
                break;
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubInterstitialLoaded) {
                    MopUpAds.getInstance(activity).showMopUpInterstitial();
                } else {
                    showSecondInterstitial();
                }
                break;
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isInterstitialLoaded()) {
                    FaceBookAds.getInstance(activity).showInterstitialAds();
                } else {
                    showSecondInterstitial();
                }
                break;
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isInterstitialLoaded()) {
                    UnityAds.getInstance(activity).showInterstitialAds();
                } else {
                    showSecondInterstitial();
                }
                break;
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isInterstitialLoaded()) {
                    ApplovinAds.getInstance(activity).showInterstitialAds();
                } else {
                    showSecondInterstitial();
                }
                break;
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isInterstitialLoaded()) {
                    IronSourceAds.getInstance(activity).showInterstitialAds();
                } else {
                    showSecondInterstitial();
                }
                break;
        }
    }

    private void showSecondInterstitial() {
        switch (SecondAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleInterstitialLoaded) {
                    GoogleAds.getInstance(activity).showGoogleInterstitial();
                } else {
                    NotshowAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubInterstitialLoaded) {
                    MopUpAds.getInstance(activity).showMopUpInterstitial();
                } else {
                    NotshowAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isInterstitialLoaded()) {
                    FaceBookAds.getInstance(activity).showInterstitialAds();
                } else {
                    NotshowAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isInterstitialLoaded()) {
                    UnityAds.getInstance(activity).showInterstitialAds();
                } else {
                    NotshowAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isInterstitialLoaded()) {
                    ApplovinAds.getInstance(activity).showInterstitialAds();
                } else {
                    NotshowAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isInterstitialLoaded()) {
                    IronSourceAds.getInstance(activity).showInterstitialAds();
                } else {
                    NotshowAd();
                }
                break;
        }
    }

    private void showFirstReward() {
        AD_Reward_COUNT++;
        String FirstRewardAd = FirstAd;
        String SecondRewardAd = SecondAd;
        if (AD_Reward_COUNT > 2) {
            if (AD_Reward_COUNT > 4) {
                AD_Reward_COUNT = 0;
            }
            FirstRewardAd = SecondAd;
            SecondRewardAd = FirstAd;
        }
        switch (FirstRewardAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleInterstitialLoaded) {
                    GoogleAds.getInstance(activity).showGoogleReward();
                } else {
                    showSecondReward();
                }
                break;
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubInterstitialLoaded) {
                    MopUpAds.getInstance(activity).showRewardAds();
                } else {
                    showSecondReward();
                }
                break;
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isRewardLoaded()) {
                    FaceBookAds.getInstance(activity).showRewardAds();
                } else {
                    showSecondReward();
                }
                break;
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isRewardLoaded()) {
                    UnityAds.getInstance(activity).showRewardAds();
                } else {
                    showSecondReward();
                }
                break;
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isRewardLoaded()) {
                    ApplovinAds.getInstance(activity).showRewardAds();
                } else {
                    showSecondReward();
                }
                break;
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isRewardLoaded()) {
                    IronSourceAds.getInstance(activity).showRewardAds();
                } else {
                    showSecondReward();
                }
                break;
        }
    }

    private void showSecondReward() {
        String FirstRewardAd = FirstAd;
        String SecondRewardAd = SecondAd;
        if (AD_Reward_COUNT > 5) {
            FirstRewardAd = SecondAd;
            SecondRewardAd = FirstAd;
        }
        switch (SecondRewardAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleInterstitialLoaded) {
                    GoogleAds.getInstance(activity).showGoogleReward();
                } else {
                    NotshowRewardAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubInterstitialLoaded) {
                    MopUpAds.getInstance(activity).showRewardAds();
                } else {
                    NotshowRewardAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isRewardLoaded()) {
                    FaceBookAds.getInstance(activity).showRewardAds();
                } else {
                    NotshowRewardAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isInterstitialLoaded()) {
                    UnityAds.getInstance(activity).showRewardAds();
                } else {
                    NotshowRewardAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isRewardLoaded()) {
                    ApplovinAds.getInstance(activity).showRewardAds();
                } else {
                    NotshowRewardAd();
                }
                break;
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isRewardLoaded()) {
                    IronSourceAds.getInstance(activity).showRewardAds();
                } else {
                    NotshowRewardAd();
                }
                break;
        }
    }

    public void showNativeLoading(LinearLayout layout) {
        if (activity != null)
            layout.addView(getLoadingView(activity));
    }

    public void showNativeAd(LinearLayout layout, ImageView img_to_hide) {
        layout.setVisibility(View.VISIBLE);
        if (SHOW_AD) {
            showFirstNative(layout, img_to_hide);
        } else {
            if (layout != null) {
                layout.setVisibility(View.GONE);
            }
        }
    }

    public boolean isNativeLoad() {
        switch (FirstAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleNativeLoaded) {
                    return true;
                } else {
                    return isSecondNativeLoad();
                }
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubNativeLoaded) {
                    return true;
                } else {
                    return isSecondNativeLoad();
                }
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isNativeLoaded()) {
                    return true;
                } else {
                    return isSecondNativeLoad();
                }
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isNativeLoaded()) {
                    return true;
                } else {
                    return isSecondNativeLoad();
                }
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isNativeLoaded()) {
                    return true;
                } else {
                    return isSecondNativeLoad();
                }
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isNativeLoaded()) {
                    return true;
                } else {
                    return isSecondNativeLoad();
                }
        }
        return true;
    }

    private boolean isSecondNativeLoad() {
        switch (SecondAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleNativeLoaded) {
                    return true;
                } else {
                    return false;
                }
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubNativeLoaded) {
                    return true;
                } else {
                    return false;
                }
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isNativeLoaded()) {
                    return true;
                } else {
                    return false;
                }
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isNativeLoaded()) {
                    return true;
                } else {
                    return false;
                }
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isNativeLoaded()) {
                    return true;
                } else {
                    return false;
                }
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isNativeLoaded()) {
                    return true;
                } else {
                    return false;
                }
        }
        return true;
    }

    private void showFirstNative(LinearLayout layout, ImageView img_to_hide) {
        switch (FirstAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleNativeLoaded) {
                    GoogleAds.getInstance(activity).showGoogleNative(layout, img_to_hide);
                } else {
                    showSecondNative(layout, img_to_hide);
                }
                break;
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubNativeLoaded) {
                    MopUpAds.getInstance(activity).showMopUpNative(layout, img_to_hide);
                } else {
                    showSecondNative(layout, img_to_hide);
                }
                break;
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isNativeLoaded()) {
                    FaceBookAds.getInstance(activity).showNative(layout, img_to_hide);
                } else {
                    showSecondNative(layout, img_to_hide);
                }
                break;
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isNativeLoaded()) {
                    UnityAds.getInstance(activity).showNative(layout, img_to_hide);
                } else {
                    showSecondNative(layout, img_to_hide);
                }
                break;
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isNativeLoaded()) {
                    ApplovinAds.getInstance(activity).showNative(layout, img_to_hide);
                } else {
                    showSecondNative(layout, img_to_hide);
                }
                break;
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isNativeLoaded()) {
                    IronSourceAds.getInstance(activity).showNative(layout, img_to_hide);
                } else {
                    showSecondNative(layout, img_to_hide);
                }
                break;
        }
    }

    private void showSecondNative(LinearLayout layout, ImageView img_to_hide) {
        switch (SecondAd) {
            case com.example.mylibrary.AdsConstant.GoogleADS:
                if (isGoogleNativeLoaded) {
                    GoogleAds.getInstance(activity).showGoogleNative(layout, img_to_hide);
                } else {
                    if (layout != null) {
//                        layout.setVisibility(View.GONE);
                    }
                }
                break;
            case com.example.mylibrary.AdsConstant.MopUpADS:
                if (isMoPubNativeLoaded) {
                    MopUpAds.getInstance(activity).showMopUpNative(layout, img_to_hide);
                } else {
                    if (layout != null) {
                        layout.setVisibility(View.GONE);
                    }
                }
                break;
            case com.example.mylibrary.AdsConstant.FaceBookADS:
                if (FaceBookAds.getInstance(activity).isNativeLoaded()) {
                    FaceBookAds.getInstance(activity).showNative(layout, img_to_hide);
                } else {
                    if (layout != null) {
                        layout.setVisibility(View.GONE);
                    }
                }
                break;
            case com.example.mylibrary.AdsConstant.UnityADS:
                if (UnityAds.getInstance(activity).isNativeLoaded()) {
                    UnityAds.getInstance(activity).showNative(layout, img_to_hide);
                } else {
                    if (layout != null) {
                        layout.setVisibility(View.GONE);
                    }
                }
                break;
            case com.example.mylibrary.AdsConstant.ApplovinADS:
                if (ApplovinAds.getInstance(activity).isNativeLoaded()) {
                    ApplovinAds.getInstance(activity).showNative(layout, img_to_hide);
                } else {
                    if (layout != null) {
                        layout.setVisibility(View.GONE);
                    }
                }
                break;
            case com.example.mylibrary.AdsConstant.IronSourceADS:
                if (IronSourceAds.getInstance(activity).isNativeLoaded()) {
                    IronSourceAds.getInstance(activity).showNative(layout, img_to_hide);
                } else {
                    if (layout != null) {
                        layout.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    private static View getBannerView(Activity activity) {
        View adView = LayoutInflater.from(activity).inflate(R.layout.banner_ad_layout_loading, null);
        ShimmerFrameLayout shimmerLayout = adView.findViewById(R.id.shimmerLayout);

        shimmerLayout.startShimmer(); // Start the shimmer effect
        shimmerLayout.setVisibility(View.VISIBLE);
        return adView;
    }

    private static View getLoadingView(Activity context) {
        View adView = LayoutInflater.from(context).inflate(R.layout.native_ad_layout_loading, null);
        ShimmerFrameLayout shimmerLayout = adView.findViewById(R.id.shimmerLayout);

        shimmerLayout.startShimmer(); // Start the shimmer effect
        shimmerLayout.setVisibility(View.VISIBLE);
        return adView;
    }
}
