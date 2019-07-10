/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.example.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import tools.StatusBarUtil;

public class ApplicationInfos extends Activity {

    private TextView skip;
    private int showTime = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.applicationinfo);

        init();
    }

    public void init(){

        skip = (TextView)findViewById(R.id.skip);

        // 修改状态栏颜色
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        StatusBarUtil.setTranslucentStatus(this);

        handler.postDelayed(runnable,showTime*1000);
        handler.sendEmptyMessage(001);

    }

    //启动页倒计时
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==001){

                skip.setText("点击跳过 "+showTime);
                showTime--;
                if(showTime>0){
                    handler.sendEmptyMessageDelayed(001,1000);
                }
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            toLoginPage();
        }
    };

    public void toLoginPage(){

        Intent intent = new Intent(this,WelcomePage.class);
        startActivity(intent);
        finish();
    }

    public void turnToLogin(View view){

        handler.removeCallbacks(runnable);
        toLoginPage();
    }

    @Override
    public void onBackPressed() {
        //  super.onBackPressed();
        Toast.makeText(this, "之后更精彩哦", Toast.LENGTH_SHORT).show();
    }
}
