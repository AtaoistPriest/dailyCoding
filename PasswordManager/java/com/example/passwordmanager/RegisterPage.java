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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import datasolve.UserInfoManager;
import objects.User;
import tools.Base64;
import tools.StatusBarUtil;

public class RegisterPage extends Activity implements View.OnClickListener {

    private EditText userAccountId,userAccountPassword;
    private TextView toLoginPage;
    private Button register;


    private long mPressedTime = 0; //用户双击返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);
        init();
    }

    private void init(){
        userAccountId = (EditText)findViewById(R.id.userRAccount);
        userAccountPassword = (EditText)findViewById(R.id.userRPassword);
        toLoginPage = (TextView)findViewById(R.id.userRLogin);
        register = (Button)findViewById(R.id.userRegister);

        toLoginPage.setOnClickListener(this);
        register.setOnClickListener(this);

        //产品ID
        Bmob.initialize(this,"338495c72de698ddd259bf683455ebe3");
        // 修改状态栏颜色
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        StatusBarUtil.setTranslucentStatus(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.userRegister:
                Log.e("-----bmob----","no");
                User user = new User();
                String userAccountIdStr = userAccountId.getText().toString();
                String userAccountPasswordStr = userAccountPassword.getText().toString();
                user.setUserAccountId(userAccountIdStr);
                user.setUserAccountPassword(userAccountPasswordStr);
                register(user);
                break;
            case R.id.userRLogin:
                Intent intent = new Intent(this,WelcomePage.class);
                startActivity(intent);
                finish();
        }
    }

    private void register(User user){


        new UserInfoManager().register(this,user);
    }

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }
        else{//退出程序
            this.finish();
            System.exit(0);
        }
    }
}
