package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.mylibrary.BaseClass;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.api.CommonClassForAPI;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ActivityTikTokBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.TiktokModel;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.AppLangSessionManager;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.SharePrefereces;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util;


import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.RootDirectoryTikTok;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.createFileFolder;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.startDownload;

public class ActivityTikTok extends AppCompatActivity {
    private ActivityTikTokBinding binding;
    ActivityTikTok activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    boolean IsWithWaternark = true;
    AppLangSessionManager appLangSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tik_tok);
        activity = this;
        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
//        MyAds.showGoogleSmartBanner(this, findViewById(R.id.banner_container_smart));
        BaseClass.getInstance(this).showBannerAd(findViewById(R.id.banner_container_smart));

        initViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        activity = this;
        assert activity != null;
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity.getSystemService(CLIPBOARD_SERVICE);

        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.imInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
            }
        });

        Glide.with(activity)
                .load(R.drawable.tt1)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity)
                .load(R.drawable.tt2)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity)
                .load(R.drawable.tt3)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity)
                .load(R.drawable.tt4)
                .into(binding.layoutHowTo.imHowto4);

        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_tiktok));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.open_tiktok));
        if (!SharePrefereces.getInstance(activity).getBoolean(SharePrefereces.ISSHOWHOWTOTT)) {
            SharePrefereces.getInstance(activity).putBoolean(SharePrefereces.ISSHOWHOWTOTT, true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }

        binding.tvWithMark.setOnClickListener(v -> {
            IsWithWaternark = true;
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Util.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Util.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                BaseClass.getInstance(this).showInterstitialAds(() -> {
                    GetTikTokData();
                });
               /* MyAds.showRewarded(this, () ->
                        GetTikTokData()
                );*/

               // showInterstitial();
            }
        });

        binding.tvWithoutMark.setOnClickListener(v -> {
            IsWithWaternark = false;
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Util.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Util.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                GetTikTokData();
               // showInterstitial();
            }
        });

        binding.LLOpenTikTok.setOnClickListener(v -> {
            Intent launchIntent = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically.go");
            Intent launchIntent1 = activity.getPackageManager().getLaunchIntentForPackage("com.zhiliaoapp.musically");
            if (launchIntent != null) {
                activity.startActivity(launchIntent);
            } else if (launchIntent1 != null) {
                activity.startActivity(launchIntent1);
            } else {
                Util.setToast(activity, getResources().getString(R.string.app_not_available));
            }

        });
    }

    private void GetTikTokData() {
        try {
            createFileFolder();
            String host = binding.etText.getText().toString();
            if (host.contains("tiktok")) {
                Util.showProgressDialog(activity);
                callVideoDownload(binding.etText.getText().toString());
            } else {
                Util.setToast(activity, "Enter Valid Url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void callVideoDownload(String Url) {
        try {
            Util util = new Util(activity);
            if (util.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    commonClassForAPI.callTiktokVideo(tiktokObserver, Url);
                }
            } else {
                Util.setToast(activity, "No Internet Connection");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DisposableObserver<TiktokModel> tiktokObserver = new DisposableObserver<TiktokModel>() {
        @Override
        public void onNext(TiktokModel tiktokModel) {
            Util.hideProgressDialog(activity);
            try {
                if (tiktokModel.getResponsecode().equals("200")) {
                    if (IsWithWaternark) {
                        startDownload(tiktokModel.getData().getMainvideo(),
                                RootDirectoryTikTok, activity, getFilenameFromURL(tiktokModel.getData().getMainvideo()));
                        binding.etText.setText("");
                    } else {
                        if (!tiktokModel.getData().getVideowithoutWaterMark().equals("")) {
                            startDownload(tiktokModel.getData().getVideowithoutWaterMark(),
                                    RootDirectoryTikTok, activity, tiktokModel.getData().getUserdetail()+"_"+System.currentTimeMillis() + ".mp4");
                            binding.etText.setText("");
                        } else {
                            startDownload(tiktokModel.getData().getMainvideo(),
                                    RootDirectoryTikTok, activity, getFilenameFromURL(tiktokModel.getData().getMainvideo()));
                            binding.etText.setText("");
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Util.hideProgressDialog(activity);
            e.printStackTrace();
            Util.showProgressDialog(activity);
            new callGetTikTokData().execute(binding.etText.getText().toString());
        }

        @Override
        public void onComplete() {
            Util.hideProgressDialog(activity);
        }
    };

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("tiktok.com")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }
                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("tiktok.com")) {
                        binding.etText.setText(item.getText().toString());
                    }
                }
            } else {
                if (CopyIntent.contains("tiktok.com")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class callGetTikTokData extends AsyncTask<String, Void, Document> {
        Document tikDoc;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Document doInBackground(String... urls) {
            try {
                tikDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return tikDoc;
        }

        protected void onPostExecute(Document result) {
            Util.hideProgressDialog(activity);
            try {
                String URL = result.select("script[id=\"__NEXT_DATA__\"]").last().html();

                if (!URL.equals("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(URL);
                        VideoUrl = String.valueOf(jsonObject.getJSONObject("props").getJSONObject("pageProps").getJSONObject("videoData").getJSONObject("itemInfos").
                                getJSONObject("video").getJSONArray("urls").get(0));
                        if (IsWithWaternark){
                            startDownload(VideoUrl, RootDirectoryTikTok, activity, "tiktok_"+System.currentTimeMillis() + ".mp4");
                        }else {
                            new GetDownloadLinkWithoutWatermark().execute();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    public String getFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath()).getName() + ".mp4";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }
    private class GetDownloadLinkWithoutWatermark extends AsyncTask<String, String, String> {
        String resp;

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(String... args) {
            try {
                this.resp = withoutWatermark(VideoUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return resp;
        }

        protected void onPostExecute(String url) {
            try {
                startDownload(url, RootDirectoryTikTok, activity, "tiktok_"+System.currentTimeMillis() + ".mp4");
                VideoUrl="";
                binding.etText.setText("");
            } catch (Exception e) {
                Util.setToast(activity, getResources().getString(R.string.error_occurred));
            }
        }
    }

    public String withoutWatermark(String url) {
        try {
            HttpURLConnection httpConn = (HttpURLConnection) new URL(url).openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            int resCode = httpConn.getResponseCode();
            BufferedReader rd = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            StringBuffer result = new StringBuffer();
            String str;
            while (true) {
                str = rd.readLine();
                if (str != null) {
                    result.append(str);
                    if (result.toString().contains("vid:")) {
                        try {
                            String VideoIdString = result.substring(result.indexOf("vid:"));
                            String VID = VideoIdString.substring(0, 4);
                            if (VID.equals("vid:")) {
                                break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                }
            }
            String VideoId = result.substring(result.indexOf("vid:"));
            String FinalVID = VideoId.substring(4, VideoId.indexOf("%")).replaceAll("[^A-Za-z0-9]","").trim();
            String finalVideoUrl = "http://api2.musical.ly/aweme/v1/play/?video_id=" + FinalVID;
            return finalVideoUrl;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}