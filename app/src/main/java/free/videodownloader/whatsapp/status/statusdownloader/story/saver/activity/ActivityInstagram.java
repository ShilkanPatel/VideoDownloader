package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.adapter.RvStoriesListAdapter;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.adapter.RvUserListAdapter;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.api.CommonClassForAPI;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ActivityInstagramBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.interfaces.UserListInterface;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.Edge;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.EdgeSidecarToChildren;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.ResponseModel;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.story.FullDetailModel;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.story.StoryModel;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.model.story.TrayModel;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.AppLangSessionManager;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.SharePrefereces;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util;

import com.example.mylibrary.BaseClass;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import io.reactivex.observers.DisposableObserver;

import static android.content.ClipDescription.MIMETYPE_TEXT_PLAIN;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.RootDirectoryInsta;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.createFileFolder;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.startDownload;

public class ActivityInstagram extends AppCompatActivity implements UserListInterface {
    Context context_vds_;
    private ActivityInstagramBinding binding;
    private ActivityInstagram activity_vds_;
    private ClipboardManager clipBoard;
    CommonClassForAPI commonClassForAPI_vds_;
    private String PhotoUrl;
    private String VideoUrl;
    AppLangSessionManager appLangSessionManager;
    RvUserListAdapter rvUserListAdapter;
    RvStoriesListAdapter rvStoriesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_instagram);
        context_vds_ = activity_vds_ = this;
        appLangSessionManager = new AppLangSessionManager(activity_vds_);
        setLocale(appLangSessionManager.getLanguage());
        commonClassForAPI_vds_ = CommonClassForAPI.getInstance(activity_vds_);
        createFileFolder();
//        MyAds.showGoogleSmartBanner(this, findViewById(R.id.banner_container_smart));
        BaseClass.getInstance(this).showBannerAd(findViewById(R.id.banner_container_smart));

        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        context_vds_ = activity_vds_ = this;
        assert activity_vds_ != null;
        clipBoard = (ClipboardManager) activity_vds_.getSystemService(CLIPBOARD_SERVICE);
        PasteText();
    }

    private void initViews() {
        clipBoard = (ClipboardManager) activity_vds_.getSystemService(CLIPBOARD_SERVICE);

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
                .load(R.drawable.insta1)
                .into(binding.layoutHowTo.imHowto1);

        Glide.with(activity_vds_)
                .load(R.drawable.insta2)
                .into(binding.layoutHowTo.imHowto2);

        Glide.with(activity_vds_)
                .load(R.drawable.insta3)
                .into(binding.layoutHowTo.imHowto3);

        Glide.with(activity_vds_)
                .load(R.drawable.insta4)
                .into(binding.layoutHowTo.imHowto4);

        binding.layoutHowTo.tvHowTo1.setText(getResources().getString(R.string.opn_insta));
        binding.layoutHowTo.tvHowTo3.setText(getResources().getString(R.string.opn_insta));
        if (!SharePrefereces.getInstance(activity_vds_).getBoolean(SharePrefereces.ISSHOWHOWTOINSTA)) {
            SharePrefereces.getInstance(activity_vds_).putBoolean(SharePrefereces.ISSHOWHOWTOINSTA,true);
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
                    GetInstagramData();
                });
//                MyAds.showRewarded(this, () ->
//                        GetInstagramData()
//                );
               // showInterstitial();
            }
        });

        binding.tvPaste.setOnClickListener(v -> {
            PasteText();
        });
        binding.LLOpenInstagram.setOnClickListener(v -> {
            Util.OpenApp(activity_vds_,"com.instagram.android");
        });

        GridLayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        binding.RVUserList.setLayoutManager(mLayoutManager);
        binding.RVUserList.setNestedScrollingEnabled(false);
        mLayoutManager.setOrientation(RecyclerView.HORIZONTAL);

        if (SharePrefereces.getInstance(activity_vds_).getBoolean(SharePrefereces.ISINSTALOGIN)) {
            layoutCondition();
            callStoriesApi();
            binding.SwitchLogin.setChecked(true);
        }else {
            binding.SwitchLogin.setChecked(false);
        }

        binding.tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_vds_, ActivityLogin.class);
                startActivityForResult(intent, 100);
            }
        });

        binding.RLLoginInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!SharePrefereces.getInstance(activity_vds_).getBoolean(SharePrefereces.ISINSTALOGIN)) {
                    Intent intent = new Intent(activity_vds_, ActivityLogin.class);
                    startActivityForResult(intent, 100);
                }else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(activity_vds_);
                    ab.setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SharePrefereces.getInstance(activity_vds_).putBoolean(SharePrefereces.ISINSTALOGIN, false);
                            SharePrefereces.getInstance(activity_vds_).putString(SharePrefereces.COOKIES, "");
                            SharePrefereces.getInstance(activity_vds_).putString(SharePrefereces.CSRF, "");
                            SharePrefereces.getInstance(activity_vds_).putString(SharePrefereces.SESSIONID, "");
                            SharePrefereces.getInstance(activity_vds_).putString(SharePrefereces.USERID, "");

                            if (SharePrefereces.getInstance(activity_vds_).getBoolean(SharePrefereces.ISINSTALOGIN)){
                                binding.SwitchLogin.setChecked(true);
                            }else {
                                binding.SwitchLogin.setChecked(false);
                                binding.RVUserList.setVisibility(View.GONE);
                                binding.RVStories.setVisibility(View.GONE);
                                binding.tvViewStories.setText(activity_vds_.getResources().getText(R.string.view_stories));
                                binding.tvLogin.setVisibility(View.VISIBLE);
                            }
                            dialog.cancel();

                        }
                    });
                    ab.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alert = ab.create();
                    alert.setTitle(getResources().getString(R.string.do_u_want_to_download_media_from_pvt));
                    alert.show();
                }

            }
        });

        GridLayoutManager mLayoutManager1 = new GridLayoutManager(getApplicationContext(), 3);
        binding.RVStories.setLayoutManager(mLayoutManager1);
        binding.RVStories.setNestedScrollingEnabled(false);
        mLayoutManager1.setOrientation(RecyclerView.VERTICAL);

    }
    public void layoutCondition(){
        binding.tvViewStories.setText(activity_vds_.getResources().getString(R.string.stories));
        binding.tvLogin.setVisibility(View.GONE);

    }

    private void GetInstagramData() {
        try {
            createFileFolder();
            URL url = new URL(binding.etText.getText().toString());
            String host = url.getHost();
            Log.e("initViews: ", host);
            if (host.equals("www.instagram.com")) {
                callDownload(binding.etText.getText().toString());
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
                if (!(clipBoard.hasPrimaryClip())) {

                } else if (!(clipBoard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {
                    if (clipBoard.getPrimaryClip().getItemAt(0).getText().toString().contains("instagram.com")) {
                        binding.etText.setText(clipBoard.getPrimaryClip().getItemAt(0).getText().toString());
                    }

                } else {
                    ClipData.Item item = clipBoard.getPrimaryClip().getItemAt(0);
                    if (item.getText().toString().contains("instagram.com")) {
                        binding.etText.setText(item.getText().toString());
                    }

                }
            } else {
                if (CopyIntent.contains("instagram.com")) {
                    binding.etText.setText(CopyIntent);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUrlWithoutParameters(String url) {
        try {
            URI uri = new URI(url);
            return new URI(uri.getScheme(),
                    uri.getAuthority(),
                    uri.getPath(),
                    null, // Ignore the query part of the input url
                    uri.getFragment()).toString();
        } catch (Exception e) {
            e.printStackTrace();
            Util.setToast(activity_vds_, getResources().getString(R.string.enter_valid_url));
            return "";
        }
    }


    private void callDownload(String Url) {
        String UrlWithoutQP = getUrlWithoutParameters(Url);
        UrlWithoutQP = UrlWithoutQP + "?__a=1";
        try {
            Util util = new Util(activity_vds_);
            if (util.isNetworkAvailable()) {
                if (commonClassForAPI_vds_ != null) {
                    Util.showProgressDialog(activity_vds_);
                    commonClassForAPI_vds_.callResult(instaObserver, UrlWithoutQP,
                            "ds_user_id="+ SharePrefereces.getInstance(activity_vds_).getString(SharePrefereces.USERID)
                                    +"; sessionid="+ SharePrefereces.getInstance(activity_vds_).getString(SharePrefereces.SESSIONID));
                }
            } else {
                Util.setToast(activity_vds_, getResources().getString(R.string.no_net_conn));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    private DisposableObserver<JsonObject> instaObserver = new DisposableObserver<JsonObject>() {
        @Override
        public void onNext(JsonObject versionList) {
            Util.hideProgressDialog(activity_vds_);
            try {
                Log.e("onNext: ", versionList.toString());
                Type listType = new TypeToken<ResponseModel>() {
                }.getType();
                ResponseModel responseModel = new Gson().fromJson(versionList.toString(), listType);
                EdgeSidecarToChildren edgeSidecarToChildren = responseModel.getGraphql().getShortcode_media().getEdge_sidecar_to_children();
                if (edgeSidecarToChildren != null) {
                    List<Edge> edgeArrayList = edgeSidecarToChildren.getEdges();
                    for (int i = 0; i < edgeArrayList.size(); i++) {
                        if (edgeArrayList.get(i).getNode().isIs_video()) {
                            VideoUrl = edgeArrayList.get(i).getNode().getVideo_url();
                            startDownload(VideoUrl, RootDirectoryInsta, activity_vds_, getVideoFilenameFromURL(VideoUrl));
                            binding.etText.setText("");
                            VideoUrl = "";

                        } else {
                            PhotoUrl = edgeArrayList.get(i).getNode().getDisplay_resources().get(edgeArrayList.get(i).getNode().getDisplay_resources().size() - 1).getSrc();
                            startDownload(PhotoUrl, RootDirectoryInsta, activity_vds_, getImageFilenameFromURL(PhotoUrl));
                            PhotoUrl = "";
                            binding.etText.setText("");
                        }
                    }
                } else {
                    boolean isVideo = responseModel.getGraphql().getShortcode_media().isIs_video();
                    if (isVideo) {
                        VideoUrl = responseModel.getGraphql().getShortcode_media().getVideo_url();
                        startDownload(VideoUrl, RootDirectoryInsta, activity_vds_, getVideoFilenameFromURL(VideoUrl));
                        VideoUrl = "";
                        binding.etText.setText("");
                    } else {
                        PhotoUrl = responseModel.getGraphql().getShortcode_media().getDisplay_resources()
                                .get(responseModel.getGraphql().getShortcode_media().getDisplay_resources().size() - 1).getSrc();

                        startDownload(PhotoUrl, RootDirectoryInsta, activity_vds_, getImageFilenameFromURL(PhotoUrl));
                        PhotoUrl = "";
                        binding.etText.setText("");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            Util.hideProgressDialog(activity_vds_);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            Util.hideProgressDialog(activity_vds_);
        }
    };

    public String getImageFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".png";
        }
    }

    public String getVideoFilenameFromURL(String url) {
        try {
            return new File(new URL(url).getPath().toString()).getName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return System.currentTimeMillis() + ".mp4";
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instaObserver.dispose();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100 && resultCode == RESULT_OK) {
                String requiredValue = data.getStringExtra("key");
                if (SharePrefereces.getInstance(activity_vds_).getBoolean(SharePrefereces.ISINSTALOGIN)){
                    binding.SwitchLogin.setChecked(true);
                    layoutCondition();
                    callStoriesApi();
                } else {
                    binding.SwitchLogin.setChecked(false);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
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

    private void callStoriesApi() {
        try {
            Util util = new Util(activity_vds_);
            if (util.isNetworkAvailable()) {
                if (commonClassForAPI_vds_ != null) {
                    binding.prLoadingBar.setVisibility(View.VISIBLE);
                    commonClassForAPI_vds_.getStories(storyObserver, "ds_user_id=" + SharePrefereces.getInstance(activity_vds_).getString(SharePrefereces.USERID)
                            + "; sessionid=" + SharePrefereces.getInstance(activity_vds_).getString(SharePrefereces.SESSIONID));
                }
            } else {
                Util.setToast(activity_vds_, activity_vds_
                        .getResources().getString(R.string.no_net_conn));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DisposableObserver<StoryModel> storyObserver = new DisposableObserver<StoryModel>() {
        @Override
        public void onNext(StoryModel response) {
            binding.RVUserList.setVisibility(View.VISIBLE);
            binding.prLoadingBar.setVisibility(View.GONE);
            try {
                rvUserListAdapter = new RvUserListAdapter(activity_vds_, response.getTray(), activity_vds_);
                binding.RVUserList.setAdapter(rvUserListAdapter);
                rvUserListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            binding.prLoadingBar.setVisibility(View.GONE);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            binding.prLoadingBar.setVisibility(View.GONE);
        }
    };

    @Override
    public void userListClick(int position, TrayModel trayModel) {
        callStoriesDetailApi(String.valueOf(trayModel.getUser().getPk()));
    }

    private void callStoriesDetailApi(String UserId) {
        try {
            Util util = new Util(activity_vds_);
            if (util.isNetworkAvailable()) {
                if (commonClassForAPI_vds_ != null) {
                    binding.prLoadingBar.setVisibility(View.VISIBLE);
                    commonClassForAPI_vds_.getFullDetailFeed(storyDetailObserver, UserId, "ds_user_id=" + SharePrefereces.getInstance(activity_vds_).getString(SharePrefereces.USERID)
                            + "; sessionid=" + SharePrefereces.getInstance(activity_vds_).getString(SharePrefereces.SESSIONID));
                }
            } else {
                Util.setToast(activity_vds_, activity_vds_
                        .getResources().getString(R.string.no_net_conn));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    private DisposableObserver<FullDetailModel> storyDetailObserver = new DisposableObserver<FullDetailModel>() {
        @Override
        public void onNext(FullDetailModel response) {
            binding.RVUserList.setVisibility(View.VISIBLE);
            binding.prLoadingBar.setVisibility(View.GONE);
            try {
                    rvStoriesListAdapter = new RvStoriesListAdapter(activity_vds_, response.getReel_feed().getItems());
                    binding.RVStories.setAdapter(rvStoriesListAdapter);
                    rvStoriesListAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(Throwable e) {
            binding.prLoadingBar.setVisibility(View.GONE);
            e.printStackTrace();
        }

        @Override
        public void onComplete() {
            binding.prLoadingBar.setVisibility(View.GONE);
        }
    };
}
