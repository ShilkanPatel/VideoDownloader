package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ActivityTwitterBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.TwitterResponse;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.AppLangSessionManager;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.SharePrefereces;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.RootDirectoryTwitter;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.createFileFolder;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.startDownload;

public class ActivityTwitter extends AppCompatActivity {
    private ActivityTwitterBinding binding;
    ActivityTwitter activity;
    CommonClassForAPI commonClassForAPI;
    private String VideoUrl;
    private ClipboardManager clipBoard;
    AppLangSessionManager appLangSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_twitter);
        activity = this;
        commonClassForAPI = CommonClassForAPI.getInstance(activity);
        createFileFolder();
//        MyAds.showGoogleSmartBanner(this, findViewById(R.id.banner_container_smart));
        BaseClass.getInstance(this).showBannerAd(findViewById(R.id.banner_container_smart));

        initViews();

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());

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
                .load(R.drawable.tw1)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity)
                .load(R.drawable.tw2)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity)
                .load(R.drawable.tw3)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity)
                .load(R.drawable.tw4)
                .into(binding.layoutHowTo.imHowto4);

        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.open_twitter));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.open_twitter));
        if (!SharePrefereces.getInstance(activity).getBoolean(SharePrefereces.ISSHOWHOWTOTWITTER)) {
            SharePrefereces.getInstance(activity).putBoolean(SharePrefereces.ISSHOWHOWTOTWITTER, true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        } else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }

        binding.loginBtn1.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Util.setToast(activity, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Util.setToast(activity, getResources().getString(R.string.enter_valid_url));
            } else {
                BaseClass.getInstance(this).showInterstitialAds(() -> {
                    Util.showProgressDialog(activity);
                    GetTwitterData();
                });
               /* MyAds.showRewarded(this, () -> {
                            Util.showProgressDialog(activity);
                            GetTwitterData();
                        }
                );*/

                //  showInterstitial();
            }
        });

        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });

        binding.LLOpenTwitter.setOnClickListener(v -> {
            Util.OpenApp(activity, "com.twitter.android");
        });
    }

    private void GetTwitterData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            if (host.contains("twitter.com")) {
                Long id = getTweetId(binding.etText.getText().toString());
                if (id != null) {
                    callGetTwitterData(String.valueOf(id));
                }
            } else {
                Util.setToast(activity, getResources().getString(R.string.enter_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Long getTweetId(String s) {
        try {
            String[] split = s.split("\\/");
            String id = split[5].split("\\?")[0];
            return Long.parseLong(id);
        } catch (Exception e) {
            Log.d("TAG", "getTweetId: " + e.getLocalizedMessage());
            return null;
        }
    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {

                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("twitter.com")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("twitter.com")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("twitter.com")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callGetTwitterData(String id) {
        String URL = "https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php";
        try {
            Util util = new Util(activity);
            if (util.isNetworkAvailable()) {
                if (commonClassForAPI != null) {
                    Util.showProgressDialog(activity);
                    commonClassForAPI.callTwitterApi(observer, URL, id);
                }
            } else {
                Util.setToast(activity, getResources().getString(R.string.no_net_conn));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private DisposableObserver<TwitterResponse> observer = new DisposableObserver<TwitterResponse>() {
        @Override
        public void onNext(TwitterResponse twitterResponse) {
            Util.hideProgressDialog(activity);
            try {
                VideoUrl = twitterResponse.getVideos().get(0).getUrl();
                if (twitterResponse.getVideos().get(0).getType().equals("image")) {
                    startDownload(VideoUrl, RootDirectoryTwitter, activity, getFilenameFromURL(VideoUrl, "image"));
                    binding.etText.setText("");
                } else {
                    VideoUrl = twitterResponse.getVideos().get(twitterResponse.getVideos().size() - 1).getUrl();
                    startDownload(VideoUrl, RootDirectoryTwitter, activity, getFilenameFromURL(VideoUrl, "mp4"));
                    binding.etText.setText("");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Util.setToast(activity, getResources().getString(R.string.no_media_on_tweet));
            }
        }

        @Override
        public void onError(Throwable e) {
            Util.hideProgressDialog(activity);
            e.printStackTrace();

        }

        @Override
        public void onComplete() {
            Util.hideProgressDialog(activity);
        }
    };


    public String getFilenameFromURL(String url, String type) {
        if (type.equals("image")) {
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".jpg";
            }
        } else {
            try {
                return new File(new URL(url).getPath()).getName() + "";
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return System.currentTimeMillis() + ".mp4";
            }
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