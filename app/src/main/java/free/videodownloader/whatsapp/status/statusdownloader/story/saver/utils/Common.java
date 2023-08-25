package free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;

public class Common {
    static ReviewManager manager;
    private static ReviewInfo reviewInfo;

    public static void ShareApp(Context context) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", context.getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=" + context.getPackageName());
        context.startActivity(Intent.createChooser(intent, "choose one"));
    }

    public static void RateApp(Activity context) {
        reviewPopUp(context, true);
    }
    public static void MoreApp(Activity context) {
        final String appPackageName = context.getPackageName(); // getPackageName() from Context or Activity object
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
    public static void UriOpen(Activity context,String uri) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(uri)));
        }
    }

    public static void RateApp(Activity context, boolean withRedirectFail) {
        reviewPopUp(context, withRedirectFail);
    }

    public static void RateAppRedirect(Activity context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(context, "unable to find app", Toast.LENGTH_SHORT).show();
        }
    }

    private static void reviewPopUp(Activity context, boolean withRedirectFail) {
        manager = ReviewManagerFactory.create(context);

        Task<ReviewInfo> request = manager.requestReviewFlow();

        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("myTag", "Review can be requested");
                reviewInfo = (ReviewInfo) task.getResult();
                startReviewFlow(context, withRedirectFail);
            } else {
                if (withRedirectFail)
                    RateAppRedirect(context);
//                Toast.makeText(context, "In App ReviewFlow failed to start", Toast.LENGTH_LONG).show();
            }
        });

    }

    public static void startReviewFlow(Activity context, boolean withRedirectFail) {
        if (reviewInfo != null) {
            Task<Void> flow = manager.launchReviewFlow(context, reviewInfo);
            flow.addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    if (withRedirectFail)
                        RateAppRedirect(context);
//                    Toast.makeText(context, "In App Rating complete", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            if (withRedirectFail)
                RateAppRedirect(context);
//            Toast.makeText(context, "In App Rating failed", Toast.LENGTH_LONG).show();
        }
    }
}
