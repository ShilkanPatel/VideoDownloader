package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.example.mylibrary.BaseClass;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.api.CommonClassForAPI;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ActivityFacebookBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.AppLangSessionManager;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.SharePrefereces;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static android.content.ContentValues.TAG;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.RootDirectoryFacebook;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.createFileFolder;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.startDownload;

public class ActivityFacebook extends AppCompatActivity {
    ActivityFacebookBinding binding;
    ActivityFacebook activity_vds_;
    CommonClassForAPI commonClassForAPI_vds_;
    private String Video_Url;
    private ClipboardManager clipBoard_vds_;
    AppLangSessionManager appLangSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_facebook);
        activity_vds_ = this;

        appLangSessionManager = new AppLangSessionManager(activity_vds_);
        setLocale(appLangSessionManager.getLanguage());
        commonClassForAPI_vds_ = CommonClassForAPI.getInstance(activity_vds_);
        createFileFolder();
//        MyAds.showGoogleSmartBanner(this, findViewById(R.id.banner_container_smart));
        BaseClass.getInstance(this).showBannerAd(findViewById(R.id.banner_container_smart));
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity_vds_ = this;
        assert activity_vds_ != null;
        clipBoard_vds_ = (ClipboardManager) activity_vds_.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initialize() {
        clipBoard_vds_ = (ClipboardManager) activity_vds_.getSystemService(CLIPBOARD_SERVICE);
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

        Glide.with(activity_vds_)
                .load(R.drawable.fb1)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity_vds_)
                .load(R.drawable.fb2)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity_vds_)
                .load(R.drawable.fb3)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity_vds_)
                .load(R.drawable.fb4)
                .into(binding.layoutHowTo.imHowto4);


        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.opn_fb));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.copy_video_link_frm_fb));

        if (!SharePrefereces.getInstance(activity_vds_).getBoolean(SharePrefereces.ISSHOWHOWTOFB)) {
            SharePrefereces.getInstance(activity_vds_).putBoolean(SharePrefereces.ISSHOWHOWTOFB,true);
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.VISIBLE);
        }else {
            binding.layoutHowTo.LLHowToLayout.setVisibility(View.GONE);
        }

        binding.loginBtn1.setOnClickListener(v -> {
            String LL = binding.etText.getText().toString();
            if (LL.equals("")) {
                Util.setToast(activity_vds_, getResources().getString(R.string.enter_url));
            } else if (!Patterns.WEB_URL.matcher(LL).matches()) {
                Util.setToast(activity_vds_, getResources().getString(R.string.enter_valid_url));
            } else {
                BaseClass.getInstance(this).showInterstitialAds(() -> {
                    GetFacebookData();
                });
//                MyAds.showRewarded(this, () ->
//                        GetFacebookData()
//                );
            }
        });

        binding.LLOpenFacebbook.setOnClickListener(v -> {
            Util.OpenApp(activity_vds_,"com.facebook.katana");
        });
        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });
    }

    private void GetFacebookData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            Log.e("initViews: ", host);

            if (host.contains("facebook.com")) {
                Util.showProgressDialog(activity_vds_);
                new callGetFacebookData().execute(binding.etText.getText().toString());
            } else {
                Util.setToast(activity_vds_, getResources().getString(R.string.enter_valid_url));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void PasteText() {
        try {
            binding.etText.setText("");
            String CopyIntent = getIntent().getStringExtra("CopyIntent");
            if (CopyIntent.equals("")) {
                if (!(clipBoard_vds_.hasPrimaryClip())) {
                } else if (!(clipBoard_vds_.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard_vds_.getPrimaryClip().getItemAt(0).getText().toString().contains("facebook.com")) {
                        binding.etText.setText(clipBoard_vds_.getPrimaryClip().getItemAt(0).getText().toString());
                    }
                } else {
                    ClipData.Item item = clipBoard_vds_.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("facebook.com")) {
                        binding.etText.setText(item.getText().toString());
                    }else {
                        Toast.makeText(this, "Enter Valid Url", Toast.LENGTH_SHORT).show();
                    }
                }
            }else {
                if (CopyIntent.contains("facebook.com")) {
                    binding.etText.setText(CopyIntent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class callGetFacebookData extends AsyncTask<String, Void, Document> {
        Document facebookDoc;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Document doInBackground(String... urls) {
            try {
                facebookDoc = Jsoup.connect(urls[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: Error");
            }
            return facebookDoc;
        }

        protected void onPostExecute(Document result) {
            Util.hideProgressDialog(activity_vds_);
            try {
                Video_Url = result.select("meta[property=\"og:video\"]").last().attr("content");
                Log.e("onPostExecute: ", Video_Url);
                if (!Video_Url.equals("")) {
                    try {
                        startDownload(Video_Url, RootDirectoryFacebook, activity_vds_, getFilenameFromURL(Video_Url));
                        Video_Url = "";
                        binding.etText.setText("");
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
            return new File(new URL(url).getPath()).getName()+".mp4";
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
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