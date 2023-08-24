package free.videodownloader.whatsapp.status.statusdownloader.story.saver.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    public String USER_PREFS = "USER PREFS";
    String WhatsApp_Uri = "WhatsApp_Uri";
    public SharedPreferences appSharedPref;
    public SharedPreferences.Editor prefEditor;

    public Pref(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER PREFS", 0);
        this.appSharedPref = sharedPreferences;
        this.prefEditor = sharedPreferences.edit();
    }

    public String get_WhatsApp_Uri() {
        return this.appSharedPref.getString(this.WhatsApp_Uri, "");
    }

    public void set_WhatsApp_Uri(String str) {
        this.prefEditor.putString(this.WhatsApp_Uri, str).commit();
    }

}
