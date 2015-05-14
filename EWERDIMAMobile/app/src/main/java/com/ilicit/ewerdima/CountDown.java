package com.ilicit.ewerdima;

/**
 * Created by Dev on 4/6/2015.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;

/**
 * Created by Dev on 4/2/2015.
 */
public class CountDown extends CountDownTimer {
    private Activity _act;
    private Class _cls;
    public CountDown(long millisInFuture, long countDownInterval, Activity act, Class cls) {
        super(millisInFuture, countDownInterval);
        _act=act;
        _cls=cls;
    }
    @Override
    public void onFinish() {
        _act.startActivity(new Intent(_act,_cls));
        _act.finish();
    }
    @Override
    public void onTick(long millisUntilFinished) {

    }
}