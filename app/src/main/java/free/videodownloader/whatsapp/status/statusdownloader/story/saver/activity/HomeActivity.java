package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mylibrary.BaseClass;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Common;

public class HomeActivity extends AppCompatActivity {
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_home);

//        MyAds.showNativeAd(this,findViewById(R.id.native_layout), null);
//        MyAds.showGoogleSmartBanner(this, findViewById(R.id.banner_container_smart));
//        BaseClass.getInstance(this).showBannerAd(findViewById(R.id.banner_container_smart));
        BaseClass.getInstance(HomeActivity.this).showNativeAd(findViewById(com.example.mylibrary.R.id.native_layout), null);
        initialize();
    }

    private void initialize() {
        findViewById(R.id.start_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseClass.getInstance(HomeActivity.this).showInterstitialAds(() -> {
                    startActivity(new Intent(HomeActivity.this, ActivityMain.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                });

//                MyAds.showInterstitial(HomeActivity.this, () ->
//                        startActivity(new Intent(HomeActivity.this, ActivityMain.class)
//                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));

            }
        });
        findViewById(R.id.share_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.ShareApp(HomeActivity.this);
            }
        });
        findViewById(R.id.rate_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.RateApp(HomeActivity.this);

//                MyAds.showInterstitial(HomeActivity.this, () ->
//                        Common.RateApp(HomeActivity.this)
//                );

            }
        });
        findViewById(R.id.more_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Common.MoreApp(HomeActivity.this);

//                MyAds.showInterstitial(HomeActivity.this, () ->
//                        Common.RateApp(HomeActivity.this)
//                );
            }
        });
    }

    @Override
    public void onBackPressed() {
        showDialog();
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this, R.style.fulldialog);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCancelable(false);
        BaseClass.getInstance(HomeActivity.this).showNativeAd(dialog.findViewById(R.id.native_layout), null);
        ((TextView) dialog.findViewById(R.id.txtYes)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                    startActivity(new Intent(HomeActivity.this, ThankYouActivity.class));
                    finish();
            }
        });
        ((TextView) dialog.findViewById(R.id.txtNo)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}