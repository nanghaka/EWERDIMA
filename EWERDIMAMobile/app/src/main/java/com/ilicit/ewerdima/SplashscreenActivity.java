package com.ilicit.ewerdima;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Dev on 4/2/2015.
 */
public class SplashscreenActivity extends Activity {





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        CountDown _tik;
        if(Utils.getSaved("Token",this).equalsIgnoreCase("")) {
            _tik = new CountDown(2000, 2000, this, LoginActivity.class); // It delay the screen for 1 second and after that switch to YourNextActivity
            _tik.start();

            Utils.save("apiUsername", "rusokoni_api", this);
            Utils.save("apipassword", "zZj0IdM0wC\"2e4M", this);
        }else{
            _tik = new CountDown(2000, 2000, this, MainActivity.class); // It delay the screen for 1 second and after that switch to YourNextActivity
            _tik.start();

            Utils.save("apiUsername", "rusokoni_api", this);
            Utils.save("apipassword", "zZj0IdM0wC\"2e4M", this);

        }

        StartAnimations();

    }




    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }

}
