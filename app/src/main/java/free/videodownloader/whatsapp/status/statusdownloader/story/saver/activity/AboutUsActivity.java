package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ActivityAboutUsBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.AppLangSessionManager;

import java.util.Locale;

import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.PrivacyPolicyUrl;

public class AboutUsActivity extends AppCompatActivity {
    ActivityAboutUsBinding binding;
    AppLangSessionManager appLangSessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us);

        appLangSessionManager = new AppLangSessionManager(AboutUsActivity.this);
        setLocale(appLangSessionManager.getLanguage());

        binding.RLPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i  = new Intent(AboutUsActivity.this, ActivityWebview.class);
                i.putExtra("URL",PrivacyPolicyUrl);
                i.putExtra("Title", getResources().getString(R.string.prv_policy));
                startActivity(i);
            }
        });
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.RLWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.website_tag)));
                    startActivity(browserIntent);
                }catch (Exception ex){
                    Log.d("WebsiteTag", "onClick: "+ex);
                }

            }
        });
        binding.RLCompanyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.RLEmail.performClick();
            }
        });
        binding.RLEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=AboutUsActivity.this.getResources().getString(R.string.email_tag);

                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients={email};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });
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
