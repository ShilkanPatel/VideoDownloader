package free.videodownloader.whatsapp.status.statusdownloader.story.saver.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.databinding.ActivityGalleryBinding;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment.FragmentFBDownloaded;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment.FragmentInstaDownloaded;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment.FragmentLikeeDownloaded;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment.FragmentRoposoDownloaded;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment.FragmentSharechatDownloaded;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment.FragmentSnackVideoDownloaded;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment.FragmentTikTokDownloaded;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment.FragmentTwitterDownloaded;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.fragment.FragmentWhatsAppDowndleded;
import free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.AppLangSessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
import static free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils.Util.createFileFolder;

import com.example.mylibrary.BaseClass;

public class ActivityGallery extends AppCompatActivity {
    AppLangSessionManager appLangSessionManager;
    ActivityGallery activity;
    ActivityGalleryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024,1024);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_gallery);
        activity = this;

        appLangSessionManager = new AppLangSessionManager(activity);
        setLocale(appLangSessionManager.getLanguage());
//        MyAds.showGoogleSmartBanner(this, findViewById(R.id.banner_container_smart));
        BaseClass.getInstance(this).showBannerAd(findViewById(R.id.banner_container_smart));

        initViews();
    }

    public void initViews() {
        setupViewPager(binding.viewpager);
        binding.tabs.setupWithViewPager(binding.viewpager);
        binding.imBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        for (int i = 0; i < binding.tabs.getTabCount(); i++) {
            TextView tv=(TextView) LayoutInflater.from(activity).inflate(R.layout.custom_tab,null);
            binding.tabs.getTabAt(i).setCustomView(tv);
        }

        binding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        createFileFolder();
    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new FragmentWhatsAppDowndleded(), "Whatsapp");
        adapter.addFragment(new FragmentInstaDownloaded(), "Instagram");
        adapter.addFragment(new FragmentFBDownloaded(), "Facebook");
        adapter.addFragment(new FragmentTikTokDownloaded(), "TikTok");
        adapter.addFragment(new FragmentSnackVideoDownloaded(), "Snack Video");
        adapter.addFragment(new FragmentSharechatDownloaded(), "Sharechat");
        adapter.addFragment(new FragmentRoposoDownloaded(), "Roposo");
        adapter.addFragment(new FragmentLikeeDownloaded(), "Likee");
        adapter.addFragment(new FragmentTwitterDownloaded(), "Twitter");


        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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
