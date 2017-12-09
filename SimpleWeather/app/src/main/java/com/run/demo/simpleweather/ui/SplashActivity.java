package com.run.demo.simpleweather.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.run.demo.simpleweather.MainActivity;
import com.run.demo.simpleweather.R;

/**
 * Created by run on 2017/12/7.
 */

public class SplashActivity extends AppCompatActivity {

    private TextView tv_splash;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1001:
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();


    }

    private void initView() {

        tv_splash = findViewById(R.id.tv_splash);





        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(SplashActivity.this, R.anim.splash_anim_set);

        //执行动画
        tv_splash.startAnimation(animationSet);

        //动画监听
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.sendEmptyMessageDelayed(1001, 500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    //取消返回键监听
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
