package com.ilicit.ewerdima;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Outline;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageButton;
import android.widget.ImageView;

public class Utils {

    public static final String MyPREFERENCES = "EWERDIMAInfo";

    /*
    public static void configureWindowEnterExitTransition(Window w) {
        Explode ex = new Explode();
        ex.setInterpolator(new PathInterpolator(0.4f, 0, 1, 1));
        w.setExitTransition(ex);
        w.setEnterTransition(ex);
    }
    */

    public static void save(String key,String value,Activity givenActivity){
        SharedPreferences prefs = getSharedPreferencesWithin(givenActivity);
        prefs.edit().putString(key, value).commit();

    }

    public static String getSaved(String key,Activity givenActivity){
        SharedPreferences prefs = getSharedPreferencesWithin(givenActivity);
        String js = prefs.getString(key, "");

        return js;

    }




    public static SharedPreferences getSharedPreferencesWithin(Activity givenActivity) {
        return givenActivity.getSharedPreferences(
                MyPREFERENCES, Context.MODE_PRIVATE);
    }




    public static void configureFab(View fabButton) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabButton.setOutlineProvider(new ViewOutlineProvider() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void getOutline(View view, Outline outline) {
                    int fabSize = view.getContext().getResources().getDimensionPixelSize(R.dimen.fab_size);
                    outline.setOval(0, 0, fabSize, fabSize);
                }
            });
        } else {
            ((ImageButton) fabButton).setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }



    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
