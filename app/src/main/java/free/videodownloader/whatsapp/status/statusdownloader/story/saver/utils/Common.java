package free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import free.videodownloader.whatsapp.status.statusdownloader.story.saver.R;

public class Common {

    public static void ShareApp(Context context) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", context.getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id="+context.getPackageName());
        context.startActivity(Intent.createChooser(intent, "choose one"));
    }

    @SuppressLint("WrongConstant")
    public static void RateApp(Context context) {
        try {
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + context.getPackageName())));
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(context, "unable to find app", 1).show();
        }
    }
}
