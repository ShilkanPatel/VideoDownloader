package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mylibrary.BaseClass;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.api.APIContent;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.api.ApiUtils;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.AppLangSessionManager;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.NetworkUtils;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.SharePrefereces;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//import com.google.android.play.core.appupdate.AppUpdateInfo;
//import com.google.android.play.core.appupdate.AppUpdateManager;
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
//import com.google.android.play.core.install.model.UpdateAvailability;
//import com.google.android.play.core.tasks.Task;

import java.io.IOException;
import java.util.Locale;

//import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class ActivitySplash extends AppCompatActivity {
    ActivitySplash activity;
    Context context;
//    AppUpdateManager appUpdateManager;
    AppLangSessionManager appLangSessionManager;
    private AppUpdateManager appUpdateManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);
        setContentView(R.layout.activity_splash_screen);
        context = activity = this;
//        appUpdateManager = AppUpdateManagerFactory.create(context);
        appLangSessionManager = new AppLangSessionManager(activity);

//        MyAds.init(context);
//        MyAds.loadGoogleInterstitialAd(activity);
//        MyAds.loadGoogleRewardedAd(activity);
//        MyAds.preloadGoogleNativeAd(activity);

//        getAdsJson();

//        HomeScreen();

        //***********************
        if (NetworkUtils.isNetworkAvailable(ActivitySplash.this)) {
            appUpdateManager = AppUpdateManagerFactory.create(ActivitySplash.this);
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, ActivitySplash.this, 0x11);
                    } catch (IntentSender.SendIntentException e) {
                        Toast.makeText(ActivitySplash.this, "Make sure you are connected to internet !!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    getData();
                }
            });
            appUpdateInfoTask.addOnFailureListener(e -> getData());

        } else {
            showInternetWarningDialog();
        }


        setLocale(appLangSessionManager.getLanguage());
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);

//        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
//            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
//                try {
//                    appUpdateManager.startUpdateFlowForResult(
//                            appUpdateInfo, IMMEDIATE, activity, 101);
//                } catch (IntentSender.SendIntentException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    public void getAdsJson() {
        try {
            ApiUtils.getAPIService(APIContent.MainUrl).getAdsJson(APIContent.GET_ADS_JSON_PHP, getPackageName()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response != null) {
                        if (response.isSuccessful()) {
                            try {
                                String jsonString = response.body().string();
                                JSONObject jsonObject = new JSONObject(jsonString);
                                BaseClass.getInstance(ActivitySplash.this).initAds(jsonObject);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    HomeScreen();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("TAG", "getAdsJson: " + t.getMessage());
                    HomeScreen();
                }
            });
        } catch (Exception e) {
            HomeScreen();
            Log.e("TAG", "getAdsJson: " + e.getMessage());
        }
    }

    public void HomeScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ActivitySplash.this, HomeActivity.class));
                finish();
            }
        }, 2500);

    }

  /*  public void UpdateApp() {
        try {
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    try {
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo, IMMEDIATE, activity, 101);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                } else {
                    HomeScreen();
                }
            }).addOnFailureListener(e -> {
                e.printStackTrace();
                HomeScreen();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode != RESULT_OK) {
//                HomeScreen();
            } else {
//                HomeScreen();
            }
        }
    }

    public void setLocale(String lang) {
        if (lang.equals("")){
            lang="en";
        }
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    //-***********************************************
    private void getData() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference project_data = database.getReference();
            project_data.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    loadAsds(snapshot.child("AdsJson").getValue().toString());
//                    loadAsds(getJsonString());
                    HomeScreen();
                    try {
                        SharePrefereces.getInstance(ActivitySplash.this).putString(SharePrefereces.MYAdsJson, snapshot.child("AdsJson").getValue().toString());
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    project_data.removeEventListener(this);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    try {
                        if (SharePrefereces.getInstance(ActivitySplash.this).getString(SharePrefereces.MYAdsJson) != null
                                && !SharePrefereces.getInstance(ActivitySplash.this).getString(SharePrefereces.MYAdsJson).isEmpty()) {
                            loadAsds(SharePrefereces.getInstance(ActivitySplash.this).getString(SharePrefereces.MYAdsJson));
                        }
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    HomeScreen();
                    Toast.makeText(ActivitySplash.this, "something went wrong ", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            showInternetWarningDialog();
        }
    }
    private void loadAsds(String jsonString) {
        JSONObject jsonObject = null;
        try {
//            String jsonString = getJsonString();
            jsonObject = new JSONObject(jsonString);
            BaseClass.getInstance(ActivitySplash.this).initAds(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private String getJsonString() {
//        return "{\n" +
//                "  \"AdShow\": \"true\",\n" +
//                "  \"ShowAd_After_Time_In_Sec\": \"1\",\n" +
//                "  \"ShowAd_After_Taps\": \"10\",\n" +
//                "  \"FirstAd\": \"Applovin\",\n" +
//                "    \"SecondAd\":\"Applovin\",\n" +
//                "\n" +
//                "  \"Google\":{\n" +
//                "  \"AppId\": \"4388627\",\n" +
//                "  \"FullScreen\": \"ca-app-pub-3940256099942544/1033173712\",\n" +
//                "  \"Banner\": \"ca-app-pub-3940256099942544/6300978111\",\n" +
//                "    \"Native\": \"ca-app-pub-3940256099942544/2247696110\",\n" +
//                "    \"RewardAD\": \"ca-app-pub-3940256099942544/5354046379\"\n" +
//                "}, \n" +
//                "  \"Applovin\":{\n" +
//                "  \"AppId\": \"\",\n" +
//                "  \"FullScreen\": \"113378a32d970069\",\n" +
//                "  \"Banner\": \"631bb8082cc91f4d\",\n" +
//                "  \"Native\": \"7515eaf4a50fdaa9\",\n" +
//                "  \"RewardAD\": \"1cd4bea52caa721d\"\n" +
//                "}\n" +
//                "}";

        //facebook applovin
//        return "{\n" +
//                "  \"AdShow\": \"true\",\n" +
//                "  \"ShowAd_After_Time_In_Sec\": \"1\",\n" +
//                "  \"ShowAd_After_Taps\": \"3\",\n" +
//                "  \"FirstAd\": \"Facebook\",\n" +
//                "    \"SecondAd\":\"Applovin\",\n" +
//                "\n" +
//                "  \"Applovin\":{\n" +
//                "  \"AppId\": \"\",\n" +
//                "  \"FullScreen\": \"113378a32d970069\",\n" +
//                "  \"Banner\": \"631bb8082cc91f4d\",\n" +
//                "    \"Native\": \"7515eaf4a50fdaa9\",\n" +
//                "    \"RewardAD\": \"1cd4bea52caa721d\"\n" +
//                "}, \n" +
//                "  \"Facebook\":{\n" +
//                "  \"AppId\": \"\",\n" +
//                "  \"FullScreen\": \"IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID\",\n" +
//                "  \"Banner\": \"IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID\",\n" +
//                "    \"Native\": \"VID_HD_9_16_39S_APP_INSTALL#YOUR_PLACEMENT_ID\",\n" +
//                "    \"RewardAD\": \"PLAYABLE#YOUR_PLACEMENT_ID\"\n" +
//                "}";
//Google facebook
        return "{\n" +
                "  \"AdShow\": \"true\",\n" +
                "  \"ShowAd_After_Time_In_Sec\": \"1\",\n" +
                "  \"ShowAd_After_Taps\": \"1\",\n" +
                "  \"FirstAd\": \"Google\",\n" +
                "    \"SecondAd\":\"Facebook\",\n" +
                "\n" +
                "  \"Google\":{\n" +
                "  \"AppId\": \"4388627\",\n" +
                "  \"FullScreen\": \"ca-app-pub-3940256099942544/1033173712\",\n" +
                "  \"Banner\": \"ca-app-pub-3940256099942544/6300978111\",\n" +
                "    \"Native\": \"ca-app-pub-3940256099942544/2247696110\",\n" +
                "    \"RewardAD\": \"ca-app-pub-3940256099942544/5354046379\"\n" +
                "}, \n" +
                "  \"Facebook\":{\n" +
                "  \"AppId\": \"\",\n" +
                "  \"FullScreen\": \"IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID\",\n" +
                "  \"Banner\": \"IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID\",\n" +
                "  \"Native\": \"VID_HD_9_16_39S_APP_INSTALL#YOUR_PLACEMENT_ID\",\n" +
                "  \"RewardAD\": \"PLAYABLE#YOUR_PLACEMENT_ID\"\n" +
                "}\n" +
                "}";
        //Unity Google
//        return "{\n" +
//                "  \"AdShow\": \"false\",\n" +
//                "  \"ShowAd_After_Time_In_Sec\": \"1\",\n" +
//                "  \"ShowAd_After_Taps\": \"1\",\n" +
//                "  \"FirstAd\": \"Unity\",\n" +
//                "    \"SecondAd\":\"Google\",\n" +
//                "\n" +
//                "  \"Google\":{\n" +
//                "  \"AppId\": \"4388627\",\n" +
//                "  \"FullScreen\": \"ca-app-pub-3940256099942544/1033173712\",\n" +
//                "  \"Banner\": \"ca-app-pub-3940256099942544/6300978111\",\n" +
//                "    \"Native\": \"ca-app-pub-3940256099942544/2247696110\",\n" +
//                "    \"RewardAD\": \"ca-app-pub-3940256099942544/5354046379\"\n" +
//                "}, \n" +
//                "  \"Unity\":{\n" +
//                "  \"AppId\": \"14851\",\n" +
//                "  \"FullScreen\": \"Interstitial_Android\",\n" +
//                "  \"Banner\": \"Banner_Android\",\n" +
//                "  \"Native\": \"VID_HD_9_16_39S_APP_INSTALL#YOUR_PLACEMENT_ID\",\n" +
//                "  \"RewardAD\": \"rewarded\"\n" +
//                "}\n" +
//                "}";

    }
    public void showInternetWarningDialog() {
        final Dialog dialog = new Dialog(ActivitySplash.this, R.style.fulldialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.no_internet_dialog);

        ImageView ivCancel = (ImageView) dialog.findViewById(R.id.ivCancel);
        ivCancel.setOnClickListener(v -> {
            finish();
            dialog.dismiss();
        });

        dialog.show();

    }
}
