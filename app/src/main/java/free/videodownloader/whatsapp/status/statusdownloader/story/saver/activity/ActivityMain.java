package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ActivityMainBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.AppLangSessionManager;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.ClipboardListener;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Common;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util;

import com.example.mylibrary.BaseClass;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.createFileFolder;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class ActivityMain extends AppCompatActivity implements View.OnClickListener {
    ActivityMain activity_vdr_;
    ActivityMainBinding binding;
    ImageView myAdsBanner;
    //    boolean doubleBackToExitPressedOnce = false;
    private ClipboardManager clipBoard_vds_;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String[] newPermissions = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
    };
    String CopyKey = "";
    String CopyValue = "";
    AppLangSessionManager appLangSessionManager_vds_;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        activity_vdr_ = this;
        appLangSessionManager_vds_ = new AppLangSessionManager(activity_vdr_);
//        MyAds.showGoogleSmartBanner(this, findViewById(R.id.banner_container_smart));
        BaseClass.getInstance(this).showBannerAd(findViewById(R.id.banner_container_smart));

        myAdsBanner = findViewById(R.id.myAdsBanner);
        myAdsBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.UriOpen(ActivityMain.this,"https://bit.ly/3NvX650");
            }
        });
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity_vdr_ = this;
        assert activity_vdr_ != null;
        clipBoard_vds_ = (ClipboardManager) activity_vdr_.getSystemService(CLIPBOARD_SERVICE);
    }

    public void initViews() {
        clipBoard_vds_ = (ClipboardManager) activity_vdr_.getSystemService(CLIPBOARD_SERVICE);
        if (activity_vdr_.getIntent().getExtras() != null) {
            for (String key : activity_vdr_.getIntent().getExtras().keySet()) {
                CopyKey = key;
                String value = activity_vdr_.getIntent().getExtras().getString(CopyKey);
                if (CopyKey.equals("android.intent.extra.TEXT")) {
                    CopyValue = activity_vdr_.getIntent().getExtras().getString(CopyKey);
                    callText(value);
                } else {
                    CopyValue = "";
                    callText(value);
                }
            }
        }
        if (clipBoard_vds_ != null) {
            clipBoard_vds_.addPrimaryClipChangedListener(new ClipboardListener() {
                @Override
                public void onPrimaryClipChanged() {
                    try {
                        showNotification(Objects.requireNonNull(clipBoard_vds_.getPrimaryClip().getItemAt(0).getText()).toString());
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions(0);
        }

        binding.rvLikee.setOnClickListener(this);
        binding.rvInsta.setOnClickListener(this);
        binding.rvWhatsApp.setOnClickListener(this);
        binding.rvTikTok.setOnClickListener(this);
        binding.rvFB.setOnClickListener(this);
        binding.rvTwitter.setOnClickListener(this);
        binding.rvGallery.setOnClickListener(this);
        binding.rvAbout.setOnClickListener(this);
        binding.rvShareApp.setOnClickListener(this);
        binding.rvRateApp.setOnClickListener(this);
        binding.rvMoreApp.setOnClickListener(this);
        binding.rvSnack.setOnClickListener(this);
        binding.rvShareChat.setOnClickListener(this);
        binding.rvRoposo.setOnClickListener(this);

        //Change language--------------->
        binding.rvChangeLang.setOnClickListener(v -> {
            final BottomSheetDialog dialogSortBy = new BottomSheetDialog(ActivityMain.this, R.style.SheetDialog);
            dialogSortBy.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogSortBy.setContentView(R.layout.dialog_language);
            final TextView tv_english = dialogSortBy.findViewById(R.id.tv_english);
            final TextView tv_hindi = dialogSortBy.findViewById(R.id.tv_hindi);
            final TextView tv_cancel = dialogSortBy.findViewById(R.id.tv_cancel);

            dialogSortBy.show();
            tv_english.setOnClickListener(view -> {
                setLocale("en");
                appLangSessionManager_vds_.setLanguage("en");
            });
            tv_hindi.setOnClickListener(view -> {
                setLocale("hi");
                appLangSessionManager_vds_.setLanguage("hi");
            });
            tv_cancel.setOnClickListener(view -> dialogSortBy.dismiss());
        });
        createFileFolder();
    }

    private void callText(String CopiedText) {
        try {
            if (CopiedText.contains("likee")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(100);
                } else {
                    callLikeeActivity();
                }
            } else if (CopiedText.contains("instagram.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
            } else if (CopiedText.contains("facebook.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
            } else if (CopiedText.contains("tiktok.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
            } else if (CopiedText.contains("twitter.com")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
            } else if (CopiedText.contains("sharechat")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(107);
                } else {
                    callShareChatActivity();
                }
            } else if (CopiedText.contains("roposo")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(108);
                } else {
                    callRoposoActivity();
                }
            } else if (CopiedText.contains("snackvideo") || CopiedText.contains("sck.io")) {
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(109);
                } else {
                    callSnackVideoActivity();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        Intent i = null;

        switch (v.getId()) {
            case R.id.rvLikee:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(100);
                } else {
                    callLikeeActivity();
                }
                break;
            case R.id.rvInsta:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(101);
                } else {
                    callInstaActivity();
                }
                break;

            case R.id.rvWhatsApp:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(102);
                } else {
                    callWhatsappActivity();
                }
                break;
            case R.id.rvTikTok:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(103);
                } else {
                    callTikTokActivity();
                }
                break;
            case R.id.rvFB:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(104);
                } else {
                    callFacebookActivity();
                }
                break;
            case R.id.rvGallery:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(105);
                } else {
                    callGalleryActivity();
                }
                break;
            case R.id.rvTwitter:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(106);
                } else {
                    callTwitterActivity();
                }
                break;
            case R.id.rvAbout:
                BaseClass.getInstance(this).showInterstitialAds(() -> {
                    Intent i2 = new Intent(activity_vdr_, AboutUsActivity.class);
                    startActivity(i2);
                });
//                MyAds.showInterstitial(this, () -> {
//                            Intent i2 = new Intent(activity_vdr_, AboutUsActivity.class);
//                            startActivity(i2);
//                        }
//                );

                break;
            case R.id.rvShareApp:
                Util.ShareApp(activity_vdr_);
                break;

            case R.id.rvRateApp:
                Util.RateApp(activity_vdr_);
                break;
            case R.id.rvMoreApp:
                Util.MoreApp(activity_vdr_);
                break;
            case R.id.rvShareChat:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(107);
                } else {
                    callShareChatActivity();
                }
                break;
            case R.id.rvRoposo:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(108);
                } else {
                    callRoposoActivity();
                }
                break;
            case R.id.rvSnack:
                if (Build.VERSION.SDK_INT >= 23) {
                    checkPermissions(109);
                } else {
                    callSnackVideoActivity();
                }
                break;

        }
    }


    public void callLikeeActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivityLikee.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });
//        MyAds.showInterstitial(this, () -> {
//            Intent i = new Intent(activity_vdr_, ActivityLikee.class);
//            i.putExtra("CopyIntent", CopyValue);
//            startActivity(i);
//                });

    }

    public void callInstaActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivityInstagram.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });
//        MyAds.showInterstitial(this, () -> {
//            Intent i = new Intent(activity_vdr_, ActivityInstagram.class);
//            i.putExtra("CopyIntent", CopyValue);
//            startActivity(i);
//        });
    }


    public void callWhatsappActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivityWhatsapp.class);
            startActivity(i);
        });
//        MyAds.showInterstitial(this, () -> {
//            Intent i = new Intent(activity_vdr_, ActivityWhatsapp.class);
//            startActivity(i);
//        });
    }

    public void callTikTokActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivityTikTok.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });
//        MyAds.showInterstitial(this, () -> {
//            Intent i = new Intent(activity_vdr_, ActivityTikTok.class);
//            i.putExtra("CopyIntent", CopyValue);
//            startActivity(i);
//        });

    }

    public void callFacebookActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivityFacebook.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });
//        MyAds.showInterstitial(this, () -> {
//            Intent i = new Intent(activity_vdr_, ActivityFacebook.class);
//            i.putExtra("CopyIntent", CopyValue);
//            startActivity(i);
//        });
    }

    public void callTwitterActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivityTwitter.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });
//        MyAds.showInterstitial(this, () -> {
//            Intent i = new Intent(activity_vdr_, ActivityTwitter.class);
//            i.putExtra("CopyIntent", CopyValue);
//            startActivity(i);
//        });
    }


    public void callGalleryActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivityGallery.class);
            startActivity(i);
        });
//        MyAds.showInterstitial(this, () -> {
//            Intent i = new Intent(activity_vdr_, ActivityGallery.class);
//            startActivity(i);
//        });
    }

    public void callRoposoActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivityRoposo.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });
//        MyAds.showInterstitial(this, () -> {
//            Intent i = new Intent(activity_vdr_, ActivityRoposo.class);
//            i.putExtra("CopyIntent", CopyValue);
//            startActivity(i);
//        });

    }

    public void callShareChatActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivityShareChat.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });
       /* MyAds.showInterstitial(this, () -> {
            Intent i = new Intent(activity_vdr_, ActivityShareChat.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });*/

    }

    public void callSnackVideoActivity() {
        BaseClass.getInstance(this).showInterstitialAds(() -> {
            Intent i = new Intent(activity_vdr_, ActivitySnackVideo.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });
        /*MyAds.showInterstitial(this, () -> {
            Intent i = new Intent(activity_vdr_, ActivitySnackVideo.class);
            i.putExtra("CopyIntent", CopyValue);
            startActivity(i);
        });*/
    }

    public void showNotification(String Text) {
        if (Text.contains("instagram.com") || Text.contains("facebook.com") || Text.contains("tiktok.com")
                || Text.contains("twitter.com") || Text.contains("likee")
                || Text.contains("sharechat") || Text.contains("roposo") || Text.contains("snackvideo") || Text.contains("sck.io")) {
            Intent intent = new Intent(activity_vdr_, ActivityMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Notification", Text);
            PendingIntent pendingIntent = PendingIntent.getActivity(activity_vdr_, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            NotificationManager notificationManager = (NotificationManager) activity_vdr_.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(getResources().getString(R.string.app_name),
                        getResources().getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                notificationManager.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder notificationBuilder;
            notificationBuilder = new NotificationCompat.Builder(activity_vdr_, getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.logo)
                    .setColor(getResources().getColor(R.color.black))
                    .setLargeIcon(BitmapFactory.decodeResource(activity_vdr_.getResources(),
                            R.mipmap.logo))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentTitle("Copied text")
                    .setContentText(Text)
                    .setChannelId(getResources().getString(R.string.app_name))
                    .setFullScreenIntent(pendingIntent, true);
            notificationManager.notify(1, notificationBuilder.build());
        }
    }

    private boolean checkPermissions(int type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p : newPermissions) {
                result = ContextCompat.checkSelfPermission(activity_vdr_, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions((Activity) (activity_vdr_),
                        listPermissionsNeeded.toArray(new
                                String[listPermissionsNeeded.size()]), type);
                return false;
            } else {
                if (type == 100) {
                    callLikeeActivity();
                } else if (type == 101) {
                    callInstaActivity();
                } else if (type == 102) {
                    callWhatsappActivity();
                } else if (type == 103) {
                    callTikTokActivity();
                } else if (type == 104) {
                    callFacebookActivity();
                } else if (type == 105) {
                    callGalleryActivity();
                } else if (type == 106) {
                    callTwitterActivity();
                } else if (type == 107) {
                    callShareChatActivity();
                } else if (type == 108) {
                    callRoposoActivity();
                } else if (type == 109) {
                    callSnackVideoActivity();
                }

            }
        } else {
            int result;
            List<String> listPermissionsNeeded = new ArrayList<>();
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(activity_vdr_, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions((Activity) (activity_vdr_),
                        listPermissionsNeeded.toArray(new
                                String[listPermissionsNeeded.size()]), type);
                return false;
            } else {
                if (type == 100) {
                    callLikeeActivity();
                } else if (type == 101) {
                    callInstaActivity();
                } else if (type == 102) {
                    callWhatsappActivity();
                } else if (type == 103) {
                    callTikTokActivity();
                } else if (type == 104) {
                    callFacebookActivity();
                } else if (type == 105) {
                    callGalleryActivity();
                } else if (type == 106) {
                    callTwitterActivity();
                } else if (type == 107) {
                    callShareChatActivity();
                } else if (type == 108) {
                    callRoposoActivity();
                } else if (type == 109) {
                    callSnackVideoActivity();
                }

            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callLikeeActivity();
            } else {
            }
            return;
        } else if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callInstaActivity();
            } else {
            }
            return;
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callWhatsappActivity();
            } else {
            }
            return;
        } else if (requestCode == 103) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTikTokActivity();
            } else {
            }
            return;
        } else if (requestCode == 104) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callFacebookActivity();
            } else {
            }
            return;
        } else if (requestCode == 105) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callGalleryActivity();
            } else {
            }
            return;
        } else if (requestCode == 106) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callTwitterActivity();
            } else {
            }
            return;
        } else if (requestCode == 107) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callShareChatActivity();
            } else {
            }
            return;
        } else if (requestCode == 108) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callRoposoActivity();
            } else {
            }
            return;
        } else if (requestCode == 109) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callSnackVideoActivity();
            } else {
            }
            return;
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ActivityMain.this, HomeActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }

    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        Intent refresh = new Intent(ActivityMain.this, ActivityMain.class);
        startActivity(refresh);
        finish();
    }
}
