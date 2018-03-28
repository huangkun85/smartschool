package hx.smartschool.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by dev on 2018/2/18.
 */

public class SharedPreferencesHelper {

    public static void clearAll(Context context, String preferencesName) {

        SharedPreferences userSettings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.clear();
        editor.commit();
    }


    public static void writeString(Context context, String preferencesName, String key, @Nullable String value) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String readString(Context context, String preferencesName, String key) {
        SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
        String strValue = settings.getString(key, null);
        return strValue;
    }


}
